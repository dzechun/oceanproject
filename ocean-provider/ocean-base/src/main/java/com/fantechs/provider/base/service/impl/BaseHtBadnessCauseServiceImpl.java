package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtBadnessCauseMapper;
import com.fantechs.provider.base.service.BaseHtBadnessCauseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class BaseHtBadnessCauseServiceImpl extends BaseService<BaseHtBadnessCause> implements BaseHtBadnessCauseService {

    @Resource
    private BaseHtBadnessCauseMapper baseHtBadnessCauseMapper;

    @Override
    public List<BaseHtBadnessCause> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtBadnessCauseMapper.findHtList(map);
    }
}
