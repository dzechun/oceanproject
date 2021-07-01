package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamReturnOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamReturnOrder;
import com.fantechs.common.base.general.entity.eam.EamReturnOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamReturnOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/06/29.
 */
@Service
public class EamReturnOrderServiceImpl extends BaseService<EamReturnOrder> implements EamReturnOrderService {

    @Resource
    private EamReturnOrderMapper eamReturnOrderMapper;
    @Resource
    private EamReturnOrderDetMapper eamReturnOrderDetMapper;
    @Resource
    private EamHtReturnOrderDetMapper eamHtReturnOrderDetMapper;
    @Resource
    private EamHtReturnOrderMapper eamHtReturnOrderMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    @Override
    public List<EamReturnOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamReturnOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamReturnOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setDeptId(user.getDeptId());
        record.setReturnUserId(user.getUserId());
        record.setReturnTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamReturnOrderMapper.insertUseGeneratedKeys(record);

        List<EamHtReturnOrderDet> eamHtReturnOrderDets = new ArrayList<>();
        List<EamReturnOrderDet> eamReturnOrderDets = record.getList();
        if(StringUtils.isNotEmpty(eamReturnOrderDets)){
            for (EamReturnOrderDet eamReturnOrderDet : eamReturnOrderDets) {
                eamReturnOrderDet.setReturnOrderId(record.getReturnOrderId());
                eamReturnOrderDet.setCreateUserId(user.getUserId());
                eamReturnOrderDet.setCreateTime(new Date());
                eamReturnOrderDet.setModifiedUserId(user.getUserId());
                eamReturnOrderDet.setModifiedTime(new Date());
                eamReturnOrderDet.setStatus(StringUtils.isEmpty(eamReturnOrderDet.getStatus())?1:eamReturnOrderDet.getStatus());
                eamReturnOrderDet.setOrgId(user.getOrganizationId());

                EamHtReturnOrderDet eamHtReturnOrderDet = new EamHtReturnOrderDet();
                BeanUtils.copyProperties(eamReturnOrderDet, eamHtReturnOrderDet);
                eamHtReturnOrderDets.add(eamHtReturnOrderDet);

                //修改设备使用状态
                EamEquipment eamEquipment = new EamEquipment();
                eamEquipment.setEquipmentId(eamReturnOrderDet.getEquipmentId());
                eamEquipment.setUsageStatus((byte)2);
                eamEquipmentMapper.updateByPrimaryKeySelective(eamEquipment);
            }
            eamReturnOrderDetMapper.insertList(eamReturnOrderDets);
            eamHtReturnOrderDetMapper.insertList(eamHtReturnOrderDets);
        }

        EamHtReturnOrder eamHtReturnOrder = new EamHtReturnOrder();
        BeanUtils.copyProperties(record, eamHtReturnOrder);
        int i = eamHtReturnOrderMapper.insert(eamHtReturnOrder);

        return i;
    }

}
