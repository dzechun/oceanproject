package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrder;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamRequisitionOrderService;
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
public class EamRequisitionOrderServiceImpl extends BaseService<EamRequisitionOrder> implements EamRequisitionOrderService {

    @Resource
    private EamRequisitionOrderMapper eamRequisitionOrderMapper;
    @Resource
    private EamRequisitionOrderDetMapper eamRequisitionOrderDetMapper;
    @Resource
    private EamHtRequisitionOrderDetMapper eamHtRequisitionOrderDetMapper;
    @Resource
    private EamHtRequisitionOrderMapper eamHtRequisitionOrderMapper;
    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    @Override
    public List<EamRequisitionOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamRequisitionOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamRequisitionOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setRequisitionTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamRequisitionOrderMapper.insertUseGeneratedKeys(record);

        List<EamHtRequisitionOrderDet> eamHtRequisitionOrderDets = new ArrayList<>();
        List<EamRequisitionOrderDet> eamRequisitionOrderDets = record.getList();
        if(StringUtils.isNotEmpty(eamRequisitionOrderDets)){
            for (EamRequisitionOrderDet eamRequisitionOrderDet : eamRequisitionOrderDets) {
                eamRequisitionOrderDet.setRequisitionOrderId(record.getRequisitionOrderId());
                eamRequisitionOrderDet.setCreateUserId(user.getUserId());
                eamRequisitionOrderDet.setCreateTime(new Date());
                eamRequisitionOrderDet.setModifiedUserId(user.getUserId());
                eamRequisitionOrderDet.setModifiedTime(new Date());
                eamRequisitionOrderDet.setStatus(StringUtils.isEmpty(eamRequisitionOrderDet.getStatus())?1:eamRequisitionOrderDet.getStatus());
                eamRequisitionOrderDet.setOrgId(user.getOrganizationId());

                EamHtRequisitionOrderDet eamHtRequisitionOrderDet = new EamHtRequisitionOrderDet();
                BeanUtils.copyProperties(eamRequisitionOrderDet, eamHtRequisitionOrderDet);
                eamHtRequisitionOrderDets.add(eamHtRequisitionOrderDet);

                //修改设备使用状态
                EamEquipment eamEquipment = new EamEquipment();
                eamEquipment.setEquipmentId(eamRequisitionOrderDet.getEquipmentId());
                eamEquipment.setUsageStatus((byte)1);
                eamEquipmentMapper.updateByPrimaryKeySelective(eamEquipment);
            }
            eamRequisitionOrderDetMapper.insertList(eamRequisitionOrderDets);
            eamHtRequisitionOrderDetMapper.insertList(eamHtRequisitionOrderDets);
        }

        EamHtRequisitionOrder eamHtRequisitionOrder = new EamHtRequisitionOrder();
        BeanUtils.copyProperties(record, eamHtRequisitionOrder);
        int i = eamHtRequisitionOrderMapper.insert(eamHtRequisitionOrder);

        return i;
    }

}
