package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRule;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInAndOutRuleMapper;
import com.fantechs.provider.base.service.BaseHtInAndOutRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */
@Service
public class BaseHtInAndOutRuleServiceImpl extends BaseService<BaseHtInAndOutRule> implements BaseHtInAndOutRuleService {

    @Resource
    private BaseHtInAndOutRuleMapper baseHtInAndOutRuleMapper;

    @Override
    public List<BaseHtInAndOutRule> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtInAndOutRuleMapper.findHtList(map);
    }
}
