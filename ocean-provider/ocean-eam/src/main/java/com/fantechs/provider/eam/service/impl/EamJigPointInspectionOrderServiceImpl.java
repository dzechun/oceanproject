package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.*;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigBarcode;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigPointInspectionProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigPointInspectionOrderService;
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
public class EamJigPointInspectionOrderServiceImpl extends BaseService<EamJigPointInspectionOrder> implements EamJigPointInspectionOrderService {

    @Resource
    private EamJigPointInspectionOrderMapper eamJigPointInspectionOrderMapper;
    @Resource
    private EamJigPointInspectionOrderDetMapper eamJigPointInspectionOrderDetMapper;
    @Resource
    private EamHtJigPointInspectionOrderMapper eamHtJigPointInspectionOrderMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigPointInspectionProjectMapper eamJigPointInspectionProjectMapper;

    @Override
    public List<EamJigPointInspectionOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamJigPointInspectionOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EamJigPointInspectionOrderDto pdaCreateOrder(String jigBarcode) {
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


        //查治具对应的点检项目
        SearchEamJigPointInspectionProject searchEamJigPointInspectionProject = new SearchEamJigPointInspectionProject();
        searchEamJigPointInspectionProject.setJigCategoryId(eamJigBarcodeDto.getJigCategoryId());
        List<EamJigPointInspectionProjectDto> list = eamJigPointInspectionProjectMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigPointInspectionProject));
        if(StringUtils.isEmpty(list)){
            throw new BizErrorException("查不到该治具所属类别的点检项目");
        }
        EamJigPointInspectionProjectDto eamJigPointInspectionProjectDto = list.get(0);


        SearchEamJigPointInspectionOrder searchEamJigPointInspectionOrder1 = new SearchEamJigPointInspectionOrder();
        searchEamJigPointInspectionOrder1.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
        searchEamJigPointInspectionOrder1.setOrderStatus((byte)1);
        List<EamJigPointInspectionOrderDto> orderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigPointInspectionOrder1));
        if(StringUtils.isNotEmpty(orderDtos)){
            throw new BizErrorException("已存在该治具待点检状态的单据");
        }


        //保存点检单信息
        EamJigPointInspectionOrder eamJigPointInspectionOrder = new EamJigPointInspectionOrder();
        List<EamJigPointInspectionOrderDetDto> eamJigPointInspectionOrderDetList = new ArrayList<>();

        eamJigPointInspectionOrder.setJigPointInspectionOrderCode(CodeUtils.getId("DJ-"));
        eamJigPointInspectionOrder.setJigId(eamJigBarcodeDto.getJigId());
        eamJigPointInspectionOrder.setJigBarcodeId(eamJigBarcodeDto.getJigBarcodeId());
        eamJigPointInspectionOrder.setJigPointInspectionProjectId(eamJigPointInspectionProjectDto.getJigPointInspectionProjectId());
        List<EamJigPointInspectionProjectItemDto> eamJigPointInspectionProjectItemList = eamJigPointInspectionProjectDto.getList();
        for (EamJigPointInspectionProjectItem eamJigPointInspectionProjectItem : eamJigPointInspectionProjectItemList){
            EamJigPointInspectionOrderDetDto eamJigPointInspectionOrderDet = new EamJigPointInspectionOrderDetDto();
            eamJigPointInspectionOrderDet.setJigPointInspectionProjectItemId(eamJigPointInspectionProjectItem.getJigPointInspectionProjectItemId());
            eamJigPointInspectionOrderDetList.add(eamJigPointInspectionOrderDet);
        }
        eamJigPointInspectionOrder.setList(eamJigPointInspectionOrderDetList);

        this.save(eamJigPointInspectionOrder);

        SearchEamJigPointInspectionOrder searchEamJigPointInspectionOrder = new SearchEamJigPointInspectionOrder();
        searchEamJigPointInspectionOrder.setJigPointInspectionOrderId(eamJigPointInspectionOrder.getJigPointInspectionOrderId());
        List<EamJigPointInspectionOrderDto> jigInspectionOrderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigPointInspectionOrder));
        return jigInspectionOrderDtos.get(0);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int pdaSubmit(EamJigPointInspectionOrder eamJigPointInspectionOrder) {
        eamJigPointInspectionOrder.setOrderStatus((byte)2);
        int i = this.update(eamJigPointInspectionOrder);

        //修改该治具使用状态
        EamJigBarcode eamJigBarcode = new EamJigBarcode();
        eamJigBarcode.setJigBarcodeId(eamJigPointInspectionOrder.getJigBarcodeId());
        eamJigBarcode.setUsageStatus((byte)2);
        eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);

        return i;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigPointInspectionOrder record) {
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
        eamJigPointInspectionOrderMapper.insertUseGeneratedKeys(record);

        //点检单事项
        List<EamJigPointInspectionOrderDetDto> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigPointInspectionOrderDet eamJigPointInspectionOrderDet : list){
                eamJigPointInspectionOrderDet.setJigPointInspectionOrderId(record.getJigPointInspectionOrderId());
                eamJigPointInspectionOrderDet.setCreateUserId(user.getUserId());
                eamJigPointInspectionOrderDet.setCreateTime(new Date());
                eamJigPointInspectionOrderDet.setModifiedUserId(user.getUserId());
                eamJigPointInspectionOrderDet.setModifiedTime(new Date());
                eamJigPointInspectionOrderDet.setStatus(StringUtils.isEmpty(eamJigPointInspectionOrderDet.getStatus())?1: eamJigPointInspectionOrderDet.getStatus());
                eamJigPointInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            eamJigPointInspectionOrderDetMapper.insertList(list);
        }

        EamHtJigPointInspectionOrder eamHtJigPointInspectionOrder = new EamHtJigPointInspectionOrder();
        BeanUtils.copyProperties(record,eamHtJigPointInspectionOrder);
        int i = eamHtJigPointInspectionOrderMapper.insert(eamHtJigPointInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigPointInspectionOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        eamJigPointInspectionOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原点检单事项
        Example example1 = new Example(EamJigPointInspectionOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigPointInspectionOrderId", entity.getJigPointInspectionOrderId());
        eamJigPointInspectionOrderDetMapper.deleteByExample(example1);

        //点检单事项
        List<EamJigPointInspectionOrderDetDto> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (EamJigPointInspectionOrderDet eamJigPointInspectionOrderDet : list){
                eamJigPointInspectionOrderDet.setJigPointInspectionOrderId(entity.getJigPointInspectionOrderId());
                eamJigPointInspectionOrderDet.setCreateUserId(user.getUserId());
                eamJigPointInspectionOrderDet.setCreateTime(new Date());
                eamJigPointInspectionOrderDet.setModifiedUserId(user.getUserId());
                eamJigPointInspectionOrderDet.setModifiedTime(new Date());
                eamJigPointInspectionOrderDet.setStatus(StringUtils.isEmpty(eamJigPointInspectionOrderDet.getStatus())?1: eamJigPointInspectionOrderDet.getStatus());
                eamJigPointInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            eamJigPointInspectionOrderDetMapper.insertList(list);
        }

        EamHtJigPointInspectionOrder eamHtJigPointInspectionOrder = new EamHtJigPointInspectionOrder();
        BeanUtils.copyProperties(entity,eamHtJigPointInspectionOrder);
        int i = eamHtJigPointInspectionOrderMapper.insert(eamHtJigPointInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<EamHtJigPointInspectionOrder> htList = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            EamJigPointInspectionOrder eamJigPointInspectionOrder = eamJigPointInspectionOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(eamJigPointInspectionOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigPointInspectionOrder eamHtJigPointInspectionOrder = new EamHtJigPointInspectionOrder();
            BeanUtils.copyProperties(eamJigPointInspectionOrder,eamHtJigPointInspectionOrder);
            htList.add(eamHtJigPointInspectionOrder);

            //删除点检单事项
            Example example1 = new Example(EamJigPointInspectionOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigPointInspectionOrderId", id);
            eamJigPointInspectionOrderDetMapper.deleteByExample(example1);
        }

        eamHtJigPointInspectionOrderMapper.insertList(htList);

        return eamJigPointInspectionOrderMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(EamJigPointInspectionOrder eamJigPointInspectionOrder){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(EamJigPointInspectionOrder.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("jigPointInspectionOrderCode",eamJigPointInspectionOrder.getJigPointInspectionOrderCode());
        if (StringUtils.isNotEmpty(eamJigPointInspectionOrder.getJigPointInspectionOrderId())){
            criteria.andNotEqualTo("jigPointInspectionOrderId",eamJigPointInspectionOrder.getJigPointInspectionOrderId());
        }
        EamJigPointInspectionOrder jigPointInspectionOrder = eamJigPointInspectionOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigPointInspectionOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }
}
