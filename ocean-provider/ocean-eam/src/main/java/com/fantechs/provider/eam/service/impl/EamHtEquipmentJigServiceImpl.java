package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJig;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtEquipmentJigMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentJigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@Service
public class EamHtEquipmentJigServiceImpl extends BaseService<EamHtEquipmentJig> implements EamHtEquipmentJigService {

    @Resource
    private EamHtEquipmentJigMapper eamHtEquipmentJigMapper;

    @Override
    public List<EamHtEquipmentJig> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentJigMapper.findList(map);
    }
}
