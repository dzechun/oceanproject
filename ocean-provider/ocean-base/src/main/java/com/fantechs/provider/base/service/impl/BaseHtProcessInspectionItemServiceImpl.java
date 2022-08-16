package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcessInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProcessInspectionItemMapper;
import com.fantechs.provider.base.service.BaseHtProcessInspectionItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class BaseHtProcessInspectionItemServiceImpl extends BaseService<BaseHtProcessInspectionItem> implements BaseHtProcessInspectionItemService {

    @Resource
    private BaseHtProcessInspectionItemMapper baseHtProcessInspectionItemMapper;

    @Override
    public List<BaseHtProcessInspectionItem> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtProcessInspectionItemMapper.findHtList(map);
    }
}
