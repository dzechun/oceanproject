package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.entity.basic.BaseWarningPersonnel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseWarningPersonnelMapper;
import com.fantechs.provider.base.service.BaseWarningPersonnelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/03.
 */
@Service
public class BaseWarningPersonnelServiceImpl extends BaseService<BaseWarningPersonnel> implements BaseWarningPersonnelService {

    @Resource
    private BaseWarningPersonnelMapper baseWarningPersonnelMapper;

    @Override
    public List<BaseWarningPersonnelDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseWarningPersonnelMapper.findList(map);
    }
}
