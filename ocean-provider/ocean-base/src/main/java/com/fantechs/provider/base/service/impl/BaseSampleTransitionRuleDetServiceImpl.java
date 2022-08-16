package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRuleDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseSampleTransitionRuleDetMapper;
import com.fantechs.provider.base.service.BaseSampleTransitionRuleDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class BaseSampleTransitionRuleDetServiceImpl extends BaseService<BaseSampleTransitionRuleDet> implements BaseSampleTransitionRuleDetService {

    @Resource
    private BaseSampleTransitionRuleDetMapper baseSampleTransitionRuleDetMapper;

    @Override
    public List<BaseSampleTransitionRuleDetDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSampleTransitionRuleDetMapper.findList(map);
    }
}
