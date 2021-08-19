package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentReEs;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqHtEquipmentReEsMapper;
import com.fantechs.provider.daq.service.DaqHtEquipmentReEsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */
@Service
public class DaqHtEquipmentReEsServiceImpl extends BaseService<EamHtEquipmentReEs> implements DaqHtEquipmentReEsService {

    @Resource
    private DaqHtEquipmentReEsMapper daqHtEquipmentReEsMapper;

    @Override
    public List<EamHtEquipmentReEsDto> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return daqHtEquipmentReEsMapper.findList(map);
    }
}
