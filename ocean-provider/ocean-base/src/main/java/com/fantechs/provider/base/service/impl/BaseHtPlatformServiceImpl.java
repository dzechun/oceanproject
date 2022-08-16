package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlatform;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPlatformMapper;
import com.fantechs.provider.base.service.BaseHtPlatformService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/14.
 */
@Service
public class BaseHtPlatformServiceImpl extends BaseService<BaseHtPlatform> implements BaseHtPlatformService {

    @Resource
    private BaseHtPlatformMapper baseHtPlatformMapper;

    @Override
    public List<BaseHtPlatform> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtPlatformMapper.findHtList(map);
    }
}
