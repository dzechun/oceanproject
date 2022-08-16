package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShop;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkShopMapper;
import com.fantechs.provider.base.service.BaseHtWorkShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/9/2.
 */
@Service
public class BaseHtWorkShopServiceImpl extends BaseService<BaseHtWorkShop> implements BaseHtWorkShopService {
    @Resource
    private BaseHtWorkShopMapper baseHtWorkShopMapper;
    @Override
    public List<BaseHtWorkShop> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtWorkShopMapper.findList(map);
    }
}
