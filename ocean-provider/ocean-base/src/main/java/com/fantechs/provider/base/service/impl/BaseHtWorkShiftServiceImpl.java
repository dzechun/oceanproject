package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkShiftMapper;
import com.fantechs.provider.base.service.BaseHtWorkShiftService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseHtWorkShiftServiceImpl extends BaseService<BaseHtWorkShift> implements BaseHtWorkShiftService {

    @Resource
    private BaseHtWorkShiftMapper baseHtWorkShiftMapper;

    @Override
    public List<BaseHtWorkShift> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtWorkShiftMapper.findHtList(map);
    }
}
