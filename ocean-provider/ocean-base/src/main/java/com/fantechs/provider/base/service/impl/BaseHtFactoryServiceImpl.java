package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtFactory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtFactoryMapper;
import com.fantechs.provider.base.service.BaseHtFactoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
@Slf4j
public class BaseHtFactoryServiceImpl extends BaseService<BaseHtFactory>  implements BaseHtFactoryService {
    @Resource
    private BaseHtFactoryMapper baseHtFactoryMapper;
    @Override
    public List<BaseHtFactory> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtFactoryMapper.findList(map);
    }
}
