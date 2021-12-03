package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigMaintainProjectMapper;
import com.fantechs.provider.eam.service.EamHtJigMaintainProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/12.
 */
@Service
public class EamHtJigMaintainProjectServiceImpl extends BaseService<EamHtJigMaintainProject> implements EamHtJigMaintainProjectService {

    @Resource
    private EamHtJigMaintainProjectMapper eamHtJigMaintainProjectMapper;

    @Override
    public List<EamHtJigMaintainProject> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamHtJigMaintainProjectMapper.findHtList(map);
    }
}
