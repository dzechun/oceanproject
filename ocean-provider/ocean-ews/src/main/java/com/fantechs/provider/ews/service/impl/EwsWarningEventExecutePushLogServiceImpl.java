package com.fantechs.provider.ews.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecutePushLogDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecutePushLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.ews.mapper.EwsWarningEventExecutePushLogMapper;
import com.fantechs.provider.ews.service.EwsWarningEventExecutePushLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/28.
 */
@Service
public class EwsWarningEventExecutePushLogServiceImpl extends BaseService<EwsWarningEventExecutePushLog> implements EwsWarningEventExecutePushLogService {

    @Resource
    private EwsWarningEventExecutePushLogMapper ewsWarningEventExecutePushLogMapper;

    @Override
    public List<EwsWarningEventExecutePushLogDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return ewsWarningEventExecutePushLogMapper.findList(map);
    }

    
}
