package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigPointInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigMaintainOrderService;
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
        //修改治具状态为停用
        eamJigBarcodeDto.setUsageStatus((byte)3);
        eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcodeDto);


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


        //保存保养单信息
        EamJigMaintainOrderDto eamJigMaintainOrderDto = new EamJigMaintainOrderDto();
        List<EamJigMaintainOrderDetDto> eamJigMaintainOrderDetList = new ArrayList<>();

        eamJigMaintainOrderDto.setJigMaintainOrderCode(CodeUtils.getId("ZJBY-"));
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
        eamJigBarcode.setUsageStatus((byte)2);
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

        this.codeIfRepeat(record);

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

        this.codeIfRepeat(entity);

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
}
