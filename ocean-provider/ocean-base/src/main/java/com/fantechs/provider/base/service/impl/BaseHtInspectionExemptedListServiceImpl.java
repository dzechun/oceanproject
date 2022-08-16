package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionExemptedListMapper;
import com.fantechs.provider.base.service.BaseHtInspectionExemptedListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/21.
 */
@Service
public class BaseHtInspectionExemptedListServiceImpl extends BaseService<BaseHtInspectionExemptedList> implements BaseHtInspectionExemptedListService {

    @Resource
    private BaseHtInspectionExemptedListMapper baseHtInspectionExemptedListMapper;

    @Override
    public List<BaseHtInspectionExemptedList> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtInspectionExemptedListMapper.findHtList(map);
    }
}
