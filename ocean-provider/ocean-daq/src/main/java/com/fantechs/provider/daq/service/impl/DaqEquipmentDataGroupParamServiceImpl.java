package com.fantechs.provider.daq.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDataGroupParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentDataGroupParam;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqEquipmentDataGroupParamMapper;
import com.fantechs.provider.daq.service.DaqEquipmentDataGroupParamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@Service
public class DaqEquipmentDataGroupParamServiceImpl extends BaseService<EamEquipmentDataGroupParam> implements DaqEquipmentDataGroupParamService {

    @Resource
    private DaqEquipmentDataGroupParamMapper daqEquipmentDataGroupParamMapper;

    @Override
    public List<EamEquipmentDataGroupParamDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return daqEquipmentDataGroupParamMapper.findList(map);
    }
}
