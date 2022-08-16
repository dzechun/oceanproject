package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWarehouseAreaMapper;
import com.fantechs.provider.base.service.BaseHtWarehouseAreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@Service
public class BaseHtWarehouseAreaServiceImpl extends BaseService<BaseHtWarehouseArea> implements BaseHtWarehouseAreaService {

    @Resource
    private BaseHtWarehouseAreaMapper baseHtWarehouseAreaMapper;
    @Override
    public List<BaseHtWarehouseArea> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtWarehouseAreaMapper.findHtList(map);
    }
}
