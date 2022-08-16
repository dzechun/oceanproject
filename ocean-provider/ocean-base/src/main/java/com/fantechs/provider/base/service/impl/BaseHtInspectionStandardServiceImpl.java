package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionStandardMapper;
import com.fantechs.provider.base.service.BaseHtInspectionStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */
@Service
public class BaseHtInspectionStandardServiceImpl extends BaseService<BaseHtInspectionStandard> implements BaseHtInspectionStandardService {

    @Resource
    private BaseHtInspectionStandardMapper baseHtInspectionStandardMapper;

    @Override
    public List<BaseHtInspectionStandard> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtInspectionStandardMapper.findHtList(map);
    }
}
