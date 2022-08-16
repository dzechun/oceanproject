package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductionKeyIssues;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductionKeyIssuesMapper;
import com.fantechs.provider.base.service.BaseHtProductionKeyIssuesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */
@Service
public class BaseHtProductionKeyIssuesServiceImpl extends BaseService<BaseHtProductionKeyIssues> implements BaseHtProductionKeyIssuesService {

    @Resource
    private BaseHtProductionKeyIssuesMapper baseHtProductionKeyIssuesMapper;

    @Override
    public List<BaseHtProductionKeyIssues> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtProductionKeyIssuesMapper.findHtList(map);
    }
}
