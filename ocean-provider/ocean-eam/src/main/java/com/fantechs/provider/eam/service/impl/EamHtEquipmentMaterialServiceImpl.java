package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtEquipmentMaterialMapper;
import com.fantechs.provider.eam.service.EamHtEquipmentMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/28.
 */
@Service
public class EamHtEquipmentMaterialServiceImpl extends BaseService<EamHtEquipmentMaterial> implements EamHtEquipmentMaterialService {

    @Resource
    private EamHtEquipmentMaterialMapper eamHtEquipmentMaterialMapper;

    @Override
    public List<EamHtEquipmentMaterial> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return eamHtEquipmentMaterialMapper.findHtList(map);
    }
}
