package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainProject;
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
    private EamJigMapper eamJigMapper;
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
        Example example = new Example(EamJigBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigBarcode",jigBarcode);
        List<EamJigBarcode> eamJigBarcodes = eamJigBarcodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(eamJigBarcodes)){
            throw new BizErrorException("查不到此治具条码");
        }

        EamJig eamJig = eamJigMapper.selectByPrimaryKey(eamJigBarcodes.get(0).getJigId());

        SearchEamJigMaintainProject searchEamJigMaintainProject = new SearchEamJigMaintainProject();
        searchEamJigMaintainProject.setJigCategoryId(eamJig.getJigCategoryId());
        List<EamJigMaintainProjectDto> list = eamJigMaintainProjectMapper.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainProject));
        if(StringUtils.isEmpty(list)){
            throw new BizErrorException("查不到该治具所属类别的保养项目");
        }
        EamJigMaintainProjectDto eamJigMaintainProjectDto = list.get(0);

        EamJigMaintainOrder eamJigMaintainOrder = new EamJigMaintainOrder();
        List<EamJigMaintainOrderDet> eamJigMaintainOrderDetList = new ArrayList<>();

        eamJigMaintainOrder.setJigMaintainOrderCode(CodeUtils.getId("BY-"));
        eamJigMaintainOrder.setJigId(eamJig.getJigId());
        eamJigMaintainOrder.setJigBarcodeId(eamJigBarcodes.get(0).getJigBarcodeId());
        eamJigMaintainOrder.setJigMaintainProjectId(eamJigMaintainProjectDto.getJigMaintainProjectId());
        List<EamJigMaintainProjectItem> eamJigMaintainProjectItemList = eamJigMaintainProjectDto.getList();
        for (EamJigMaintainProjectItem eamJigMaintainProjectItem : eamJigMaintainProjectItemList){
            EamJigMaintainOrderDet eamJigMaintainOrderDet = new EamJigMaintainOrderDet();
            eamJigMaintainOrderDet.setJigMaintainProjectItemId(eamJigMaintainProjectItem.getJigMaintainProjectItemId());
            eamJigMaintainOrderDetList.add(eamJigMaintainOrderDet);
        }
        eamJigMaintainOrder.setList(eamJigMaintainOrderDetList);

        this.save(eamJigMaintainOrder);

        SearchEamJigMaintainOrder searchEamJigMaintainOrder = new SearchEamJigMaintainOrder();
        searchEamJigMaintainOrder.setJigMaintainOrderId(eamJigMaintainOrder.getJigMaintainOrderId());
        List<EamJigMaintainOrderDto> jigMaintainOrderDtos = this.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainOrder));
        return jigMaintainOrderDtos.get(0);
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
        List<EamJigMaintainOrderDet> list = record.getList();
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
        List<EamJigMaintainOrderDet> list = entity.getList();
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
