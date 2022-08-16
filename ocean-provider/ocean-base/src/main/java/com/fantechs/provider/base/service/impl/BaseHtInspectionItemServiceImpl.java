package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionItemMapper;
import com.fantechs.provider.base.service.BaseHtInspectionItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/03.
 */
@Service
public class BaseHtInspectionItemServiceImpl extends BaseService<BaseHtInspectionItem> implements BaseHtInspectionItemService {

    @Resource
    private BaseHtInspectionItemMapper baseHtInspectionItemMapper;

    @Override
    public List<BaseHtInspectionItem> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtInspectionItemMapper.findHtList(map);
    }
}
