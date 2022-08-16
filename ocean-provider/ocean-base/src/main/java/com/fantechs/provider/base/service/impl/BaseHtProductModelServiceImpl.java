package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductModel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductModelMapper;
import com.fantechs.provider.base.service.BaseHtProductModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BaseHtProductModelServiceImpl extends BaseService<BaseHtProductModel> implements BaseHtProductModelService {

    @Resource
    private BaseHtProductModelMapper baseHtProductModelMapper;

    @Override
    public List<BaseHtProductModel> selectHtProductModels(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtProductModelMapper.selectHtProductModels(map);
    }
}
