package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageCapacity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.base.mapper.BaseHtStorageCapacityMapper;
import com.fantechs.provider.base.service.BaseHtStorageCapacityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/18.
 */
@Service
public class BaseHtStorageCapacityServiceImpl extends BaseService<BaseHtStorageCapacity> implements BaseHtStorageCapacityService {

    @Resource
    private BaseHtStorageCapacityMapper baseHtStorageCapacityMapper;

    @Override
    public List<BaseHtStorageCapacity> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtStorageCapacityMapper.findHtList(map);
    }
}
