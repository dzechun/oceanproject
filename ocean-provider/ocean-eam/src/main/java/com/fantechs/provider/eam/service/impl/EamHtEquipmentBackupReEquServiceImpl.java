package com.fantechs.provider.eam.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentBackupReEqu;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtEquipmentBackupReEquMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentBackupReEquService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */
@Service
public class EamHtEquipmentBackupReEquServiceImpl extends BaseService<EamHtEquipmentBackupReEqu> implements EamHtEquipmentBackupReEquService {

    @Resource
    private EamHtEquipmentBackupReEquMapper eamHtEquipmentBackupReEquMapper;

    @Override
    public List<EamHtEquipmentBackupReEqu> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());

        return eamHtEquipmentBackupReEquMapper.findHtList(map);
    }
}
