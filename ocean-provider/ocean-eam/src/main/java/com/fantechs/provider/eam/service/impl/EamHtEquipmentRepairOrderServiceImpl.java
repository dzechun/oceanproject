package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentRepairOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtEquipmentRepairOrderMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentRepairOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@Service
public class EamHtEquipmentRepairOrderServiceImpl extends BaseService<EamHtEquipmentRepairOrder> implements EamHtEquipmentRepairOrderService {

    @Resource
    private EamHtEquipmentRepairOrderMapper eamHtEquipmentRepairOrderMapper;

    @Override
    public List<EamHtEquipmentRepairOrder> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamHtEquipmentRepairOrderMapper.findList(map);
    }
}
