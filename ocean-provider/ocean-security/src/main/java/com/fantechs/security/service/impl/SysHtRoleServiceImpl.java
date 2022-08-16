package com.fantechs.security.service.impl;

import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.security.mapper.SysHtRoleMapper;
import com.fantechs.security.service.SysHtRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysHtRoleServiceImpl extends BaseService<SysHtRole> implements SysHtRoleService {

    @Resource
    private SysHtRoleMapper sysHtRoleMapper;

    @Override
    public List<SysHtRole> selectHtRoles(SearchSysRole searchSysRole) {
        return sysHtRoleMapper.selectHtRoles(searchSysRole);
    }
}
