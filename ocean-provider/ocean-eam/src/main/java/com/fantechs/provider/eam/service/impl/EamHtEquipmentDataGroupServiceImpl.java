package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentDataGroupDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentDataGroup;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtEquipmentDataGroupMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentDataGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class EamHtEquipmentDataGroupServiceImpl extends BaseService<EamHtEquipmentDataGroup> implements EamHtEquipmentDataGroupService {

    @Resource
    private EamHtEquipmentDataGroupMapper eamHtEquipmentDataGroupMapper;

    @Override
    public List<EamHtEquipmentDataGroupDto> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentDataGroupMapper.findList(map);
    }
}
