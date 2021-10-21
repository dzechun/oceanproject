package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigMaintainOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/08/13.
 */
@Service
public class EamJigMaintainOrderServiceImpl extends BaseService<EamJigMaintainOrder> implements EamJigMaintainOrderService {

    @Resource
    private EamJigMaintainOrderMapper eamJigMaintainOrderMapper;
    @Resource
    private EamJigMaintainOrderDetMapper eamJigMaintainOrderDetMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigMaintainProjectMapper eamJigMaintainProjectMapper;
    @Resource
    private EamHtJigMaintainOrderMapper eamHtJigMaintainOrderMapper;
    @Resource
    private EamJigPointInspectionOrderMapper eamJigPointInspectionOrderMapper;
    @Resource
    private EamJigRequisitionMapper eamJigRequisitionMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<EamJigMaintainOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigMaintainOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EamJigMaintainOrderDto pdaCreateOrder(String jigBarcode) {
        //查治具条码信息
        SearchEamJigBarcode searchEamJigBarcode = new SearchEamJigBarcode();
        searchEamJigBarcode.setJigBarcode(jigBarcode);
        List<EamJigBarcodeDto> eamJigBarcodeDtos = eamJigBarcodeMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigBarcode));
        if(StringUtils.isEmpty(eamJigBarcodeDtos)){
            throw new BizErrorException("查不到此治具条码");
        }
        EamJigBarcodeDto eamJigBarcodeDto = eamJigBarcodeDtos.get(0);


        //查治具对应的保养项目
        SearchEamJigMaintainProject searchEamJigMaintainProject = new SearchEamJigMaintainProject();
        searchEamJigMaintainProject.setJigCategoryId(eamJigBarcodeDto.getJigCategoryId());
        List<EamJigMaintainProjectDto> list = eamJigMaintainProjectMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainProject));
        if(StringUtils.isEmpty(list)){
            throw new BizErrorException("查不到该治具所属类别的保养项目");
        }
        EamJigMaintainProjectDto eamJigMaintainProjectDto = list.get(0);

        SearchEamJigMaintainOrder searchEamJigMaintainOrder = new SearchEamJigMaintainOrder();
        searchEamJigMaintainOrder.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
        searchEamJigMaintainOrder.setOrderStatus((byte)1);
        List<EamJigMaintainOrderDto> orderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainOrder));
        if(StringUtils.isNotEmpty(orderDtos)){
            throw new BizErrorException("已存在该治具待保养状态的单据");
        }

        Example example = new Example(EamJigPointInspectionOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcodeId",eamJigBarcodeDto.getJigBarcodeId())
                .andEqualTo("orderStatus",1);
        EamJigPointInspectionOrder eamJigPointInspectionOrder = eamJigPointInspectionOrderMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(eamJigPointInspectionOrder)){
            throw new BizErrorException("保养与点检不能同时进行");
        }

        //保存保养单信息
        EamJigMaintainOrderDto eamJigMaintainOrderDto = new EamJigMaintainOrderDto();
        List<EamJigMaintainOrderDetDto> eamJigMaintainOrderDetList = new ArrayList<>();

        eamJigMaintainOrderDto.setJigId(eamJigBarcodeDto.getJigId());
        eamJigMaintainOrderDto.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
        eamJigMaintainOrderDto.setJigMaintainProjectId(eamJigMaintainProjectDto.getJigMaintainProjectId());
        List<EamJigMaintainProjectItemDto> eamJigMaintainProjectItemList = eamJigMaintainProjectDto.getList();
        for (EamJigMaintainProjectItem eamJigMaintainProjectItem : eamJigMaintainProjectItemList){
            EamJigMaintainOrderDetDto eamJigMaintainOrderDetDto = new EamJigMaintainOrderDetDto();
            eamJigMaintainOrderDetDto.setJigMaintainProjectItemId(eamJigMaintainProjectItem.getJigMaintainProjectItemId());
            eamJigMaintainOrderDetList.add(eamJigMaintainOrderDetDto);
        }
        eamJigMaintainOrderDto.setList(eamJigMaintainOrderDetList);

        this.save(eamJigMaintainOrderDto);


        SearchEamJigMaintainOrder searchEamJigMaintainOrder1 = new SearchEamJigMaintainOrder();
        searchEamJigMaintainOrder1.setJigMaintainOrderId(eamJigMaintainOrderDto.getJigMaintainOrderId());
        List<EamJigMaintainOrderDto> eamJigMaintainOrderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainOrder1));
        return eamJigMaintainOrderDtos.get(0);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pdaSubmit(EamJigMaintainOrder eamJigMaintainOrder) {
        eamJigMaintainOrder.setOrderStatus((byte)2);
        int i = this.update(eamJigMaintainOrder);

        //修改该治具保养信息
        EamJigBarcode eamJigBarcode = new EamJigBarcode();
        eamJigBarcode.setJigBarcodeId(eamJigMaintainOrder.getJigBarcodeId());
        eamJigBarcode.setLastTimeMaintainTime(new Date());
        eamJigBarcode.setCurrentMaintainTime(eamJigBarcode.getCurrentMaintainTime()==null?1:eamJigBarcode.getCurrentMaintainTime()+1);
        eamJigBarcode.setCurrentMaintainUsageTime(0);
        Map<String,Object> map = new HashMap<>();
        map.put("jigBarcodeId",eamJigMaintainOrder.getJigBarcodeId());
        map.put("requisitionStatus",1);
        if(StringUtils.isNotEmpty(eamJigRequisitionMapper.findList(map))){
            eamJigBarcode.setUsageStatus((byte)2);
        }else {
            eamJigBarcode.setUsageStatus((byte) 1);
        }
        eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigMaintainOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectByPrimaryKey(record.getJigBarcodeId());
        if(eamJigBarcode.getUsageStatus()!=1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"治具非空闲状态无法新建保养单");
        }
        //修改该治具使用状态
        eamJigBarcode.setUsageStatus((byte)4);
        eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);

        //this.codeIfRepeat(record);

        //获取保养单号编码规则 jigMaintainOrderCodeRule
        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
        searchBaseBarcodeRule.setBarcodeRuleCode("jigMaintainOrderCodeRule");
        List<BaseBarcodeRuleDto> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
        if(StringUtils.isEmpty(barcodeRulList)) throw new BizErrorException("未配置治具保养单号编码规则 jigMaintainOrderCodeRule");

        SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        searchBaseBarcodeRuleSpec.setBarcodeRuleId(barcodeRulList.get(0).getBarcodeRuleId());
        ResponseEntity<List<BaseBarcodeRuleSpec>> barcodeRuleSpecList= baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
        if(barcodeRuleSpecList.getCode()!=0) throw new BizErrorException(barcodeRuleSpecList.getMessage());
        if(barcodeRuleSpecList.getData().size()<1) throw new BizErrorException("请设置条码规则");
        List<BaseBarcodeRuleSpec>  barcodeRuleSpecsList = barcodeRuleSpecList.getData();

        //生成条码
        String barCode=creatBarCode(barcodeRulList.get(0),barcodeRuleSpecsList);
        if(StringUtils.isEmpty(barCode))
            throw new BizErrorException("生成保养单号出错");

        record.setJigMaintainOrderCode(barCode);
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus((byte)1);
        eamJigMaintainOrderMapper.insertUseGeneratedKeys(record);

        //保养单事项
        List<EamJigMaintainOrderDetDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaintainOrderDet eamJigMaintainOrderDet : list){
                eamJigMaintainOrderDet.setJigMaintainOrderId(record.getJigMaintainOrderId());
                eamJigMaintainOrderDet.setCreateUserId(user.getUserId());
                eamJigMaintainOrderDet.setCreateTime(new Date());
                eamJigMaintainOrderDet.setModifiedUserId(user.getUserId());
                eamJigMaintainOrderDet.setModifiedTime(new Date());
                eamJigMaintainOrderDet.setStatus(StringUtils.isEmpty(eamJigMaintainOrderDet.getStatus())?1: eamJigMaintainOrderDet.getStatus());
                eamJigMaintainOrderDet.setOrgId(user.getOrganizationId());
            }
            eamJigMaintainOrderDetMapper.insertList(list);
        }

        EamHtJigMaintainOrder eamHtJigMaintainOrder = new EamHtJigMaintainOrder();
        BeanUtils.copyProperties(record,eamHtJigMaintainOrder);
        int i = eamHtJigMaintainOrderMapper.insert(eamHtJigMaintainOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigMaintainOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigMaintainOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原保养单事项
        Example example1 = new Example(EamJigMaintainOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigMaintainOrderId", entity.getJigMaintainOrderId());
        eamJigMaintainOrderDetMapper.deleteByExample(example1);

        //保养单事项
        List<EamJigMaintainOrderDetDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigMaintainOrderDet eamJigMaintainOrderDet : list){
                eamJigMaintainOrderDet.setJigMaintainOrderId(entity.getJigMaintainOrderId());
                eamJigMaintainOrderDet.setCreateUserId(user.getUserId());
                eamJigMaintainOrderDet.setCreateTime(new Date());
                eamJigMaintainOrderDet.setModifiedUserId(user.getUserId());
                eamJigMaintainOrderDet.setModifiedTime(new Date());
                eamJigMaintainOrderDet.setStatus(StringUtils.isEmpty(eamJigMaintainOrderDet.getStatus())?1: eamJigMaintainOrderDet.getStatus());
                eamJigMaintainOrderDet.setOrgId(user.getOrganizationId());
            }
            eamJigMaintainOrderDetMapper.insertList(list);
        }

        EamHtJigMaintainOrder eamHtJigMaintainOrder = new EamHtJigMaintainOrder();
        BeanUtils.copyProperties(entity,eamHtJigMaintainOrder);
        int i = eamHtJigMaintainOrderMapper.insert(eamHtJigMaintainOrder);

        return i;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigMaintainOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigMaintainOrder eamJigMaintainOrder = eamJigMaintainOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigMaintainOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigMaintainOrder eamHtJigMaintainOrder = new EamHtJigMaintainOrder();
            BeanUtils.copyProperties(eamJigMaintainOrder,eamHtJigMaintainOrder);
            htList.add(eamHtJigMaintainOrder);

            //删除保养单事项
            Example example1 = new Example(EamJigMaintainOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigMaintainOrderId", id);
            eamJigMaintainOrderDetMapper.deleteByExample(example1);
        }

        eamHtJigMaintainOrderMapper.insertList(htList);

        return eamJigMaintainOrderMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigMaintainOrder eamJigMaintainOrder){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigMaintainOrder.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("jigMaintainOrderCode",eamJigMaintainOrder.getJigMaintainOrderCode());
        if (StringUtils.isNotEmpty(eamJigMaintainOrder.getJigMaintainOrderId())){
            criteria.andNotEqualTo("jigMaintainOrderId",eamJigMaintainOrder.getJigMaintainOrderId());
        }
        EamJigMaintainOrder jigMaintainOrder = eamJigMaintainOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigMaintainOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    //生成条码
    private String creatBarCode(BaseBarcodeRuleDto baseBarcodeRuleDto,List<BaseBarcodeRuleSpec> list){
        String lastBarCode = null;

        boolean hasKey = redisUtil.hasKey(baseBarcodeRuleDto.getBarcodeRule());
        if(hasKey){
            // 从redis获取上次生成条码
            Object redisRuleData = redisUtil.get(baseBarcodeRuleDto.getBarcodeRule());
            lastBarCode = String.valueOf(redisRuleData);
        }
        //获取最大流水号
        String maxCode = baseFeignApi.generateMaxCode(list, lastBarCode).getData();
        //生成条码
        ResponseEntity<String> rs = baseFeignApi.generateCode(list,maxCode,"","");
        if(rs.getCode()!=0){
            throw new BizErrorException(rs.getMessage());
        }

        // 更新redis最新条码
        redisUtil.set(baseBarcodeRuleDto.getBarcodeRule(), rs.getData());
        return rs.getData();
    }
}
