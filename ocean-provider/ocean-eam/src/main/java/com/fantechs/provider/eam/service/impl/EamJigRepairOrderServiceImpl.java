package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderDto;
import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderReplacementDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRepairOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRepairOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigRepairOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */
@Service
public class EamJigRepairOrderServiceImpl extends BaseService<EamJigRepairOrder> implements EamJigRepairOrderService {

    @Resource
    private EamJigRepairOrderMapper eamJigRepairOrderMapper;
    @Resource
    private EamJigRepairOrderReplacementMapper eamJigRepairOrderReplacementMapper;
    @Resource
    private EamHtJigRepairOrderMapper eamHtJigRepairOrderMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;


    @Override
    public List<EamJigRepairOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamJigRepairOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EamJigRepairOrderDto pdaCreateOrder(String jigBarcode) {
        SearchEamJigBarcode searchEamJigBarcode = new SearchEamJigBarcode();
        searchEamJigBarcode.setJigBarcode(jigBarcode);
        searchEamJigBarcode.setCodeQueryMark(1);
        List<EamJigBarcodeDto> jigBarcodeDtos = eamJigBarcodeMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigBarcode));
        if(StringUtils.isEmpty(jigBarcodeDtos)){
            throw new BizErrorException("查不到此治具条码");
        }
        EamJigBarcodeDto eamJigBarcodeDto = jigBarcodeDtos.get(0);

        SearchEamJigRepairOrder searchEamJigRepairOrder1 = new SearchEamJigRepairOrder();
        searchEamJigRepairOrder1.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
        searchEamJigRepairOrder1.setOrderStatus((byte)1);
        List<EamJigRepairOrderDto> orderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRepairOrder1));
        if(StringUtils.isNotEmpty(orderDtos)){
            throw new BizErrorException("已存在该治具待维修状态的单据");
        }

        //维修单信息
        EamJigRepairOrderDto eamJigRepairOrderDto = new EamJigRepairOrderDto();
        eamJigRepairOrderDto.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
        eamJigRepairOrderDto.setJigBarcode(jigBarcode);
        eamJigRepairOrderDto.setJigId(eamJigBarcodeDto.getJigId());
        eamJigRepairOrderDto.setJigCode(eamJigBarcodeDto.getJigCode());
        eamJigRepairOrderDto.setJigName(eamJigBarcodeDto.getJigName());

        return eamJigRepairOrderDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigRepairOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //报废无法新增维修单
        EamJigBarcode eamJigBarcode = eamJigBarcodeMapper.selectByPrimaryKey(record.getJigBarcodeId());
        if(eamJigBarcode.getUsageStatus()==6){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"治具非空闲无法新建维修单");
        }
        //修改该治具使用状态
        eamJigBarcode.setUsageStatus((byte)5);
        eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);

        record.setJigRepairOrderCode(CodeUtils.getId("ZJWX-"));
        record.setRequestForRepairTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setOrgId(user.getOrganizationId());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrderStatus((byte)1);
        eamJigRepairOrderMapper.insertUseGeneratedKeys(record);

        //治具维修单替换件
        List<EamJigRepairOrderReplacementDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigRepairOrderReplacement eamJigRepairOrderReplacement : list){
                eamJigRepairOrderReplacement.setJigRepairOrderId(record.getJigRepairOrderId());
                eamJigRepairOrderReplacement.setCreateUserId(user.getUserId());
                eamJigRepairOrderReplacement.setCreateTime(new Date());
                eamJigRepairOrderReplacement.setModifiedUserId(user.getUserId());
                eamJigRepairOrderReplacement.setModifiedTime(new Date());
                eamJigRepairOrderReplacement.setStatus(StringUtils.isEmpty(eamJigRepairOrderReplacement.getStatus())?1: eamJigRepairOrderReplacement.getStatus());
                eamJigRepairOrderReplacement.setOrgId(user.getOrganizationId());
            }
            eamJigRepairOrderReplacementMapper.insertList(list);
        }

        EamHtJigRepairOrder eamHtJigRepairOrder = new EamHtJigRepairOrder();
        BeanUtils.copyProperties(record,eamHtJigRepairOrder);
        int i = eamHtJigRepairOrderMapper.insertSelective(eamHtJigRepairOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigRepairOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setRepairTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigRepairOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原治具维修单替换件
        Example example1 = new Example(EamJigRepairOrderReplacement.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigRepairOrderId", entity.getJigRepairOrderId());
        eamJigRepairOrderReplacementMapper.deleteByExample(example1);

        //治具维修单替换件
        List<EamJigRepairOrderReplacementDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigRepairOrderReplacement eamJigRepairOrderReplacement : list){
                eamJigRepairOrderReplacement.setJigRepairOrderId(entity.getJigRepairOrderId());
                eamJigRepairOrderReplacement.setCreateUserId(user.getUserId());
                eamJigRepairOrderReplacement.setCreateTime(new Date());
                eamJigRepairOrderReplacement.setModifiedUserId(user.getUserId());
                eamJigRepairOrderReplacement.setModifiedTime(new Date());
                eamJigRepairOrderReplacement.setStatus(StringUtils.isEmpty(eamJigRepairOrderReplacement.getStatus())?1: eamJigRepairOrderReplacement.getStatus());
                eamJigRepairOrderReplacement.setOrgId(user.getOrganizationId());
            }
            eamJigRepairOrderReplacementMapper.insertList(list);
        }

        EamHtJigRepairOrder eamHtJigRepairOrder = new EamHtJigRepairOrder();
        BeanUtils.copyProperties(entity,eamHtJigRepairOrder);
        int i = eamHtJigRepairOrderMapper.insertSelective(eamHtJigRepairOrder);

        updateJigUsageStatus(entity);

        return i;
    }

    /**
     * 修改该治具使用状态
     * @param entity
     */
    public void updateJigUsageStatus(EamJigRepairOrder entity) {
        if(StringUtils.isNotEmpty(entity.getRepairUserId())) {
            entity.setOrderStatus((byte)3);
            eamJigRepairOrderMapper.updateByPrimaryKeySelective(entity);

            EamJigBarcode eamJigBarcode = new EamJigBarcode();
            eamJigBarcode.setJigBarcodeId(entity.getJigBarcodeId());
            eamJigBarcode.setUsageStatus((byte) 1);
            eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJigRepairOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigRepairOrder eamJigRepairOrder = eamJigRepairOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigRepairOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigRepairOrder eamHtJigRepairOrder = new EamHtJigRepairOrder();
            BeanUtils.copyProperties(eamJigRepairOrder,eamHtJigRepairOrder);
            htList.add(eamHtJigRepairOrder);

            //删除治具维修单替换件
            Example example1 = new Example(EamJigRepairOrderReplacement.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigRepairOrderId", id);
            eamJigRepairOrderReplacementMapper.deleteByExample(example1);
        }

        eamHtJigRepairOrderMapper.insertList(htList);

        return eamJigRepairOrderMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigRepairOrder eamJigRepairOrder){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamJigRepairOrder.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("jigRepairOrderCode",eamJigRepairOrder.getJigRepairOrderCode());
        if (StringUtils.isNotEmpty(eamJigRepairOrder.getJigRepairOrderId())){
            criteria.andNotEqualTo("jigRepairOrderId",eamJigRepairOrder.getJigRepairOrderId());
        }
        EamJigRepairOrder jigRepairOrder = eamJigRepairOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigRepairOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }
}