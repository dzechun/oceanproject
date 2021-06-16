package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProducttionKeyIssues;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProducttionKeyIssuesMapper;
import com.fantechs.provider.base.service.BaseHtProducttionKeyIssuesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */
@Service
public class BaseHtProducttionKeyIssuesServiceImpl extends BaseService<BaseHtProducttionKeyIssues> implements BaseHtProducttionKeyIssuesService {

    @Resource
    private BaseHtProducttionKeyIssuesMapper baseHtProducttionKeyIssuesMapper;

    @Override
    public List<BaseHtProducttionKeyIssues> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseHtProducttionKeyIssuesMapper.findHtList(map);
    }
}
