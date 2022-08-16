package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleStandard;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSampleStandardMapper;
import com.fantechs.provider.base.service.BaseHtSampleStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/06.
 */
@Service
public class BaseHtSampleStandardServiceImpl extends BaseService<BaseHtSampleStandard> implements BaseHtSampleStandardService {

    @Resource
    private BaseHtSampleStandardMapper baseHtSampleStandardMapper;

    @Override
    public List<BaseHtSampleStandard> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtSampleStandardMapper.findHtList(map);
    }
}
