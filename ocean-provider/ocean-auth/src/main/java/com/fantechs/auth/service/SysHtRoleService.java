package com.fantechs.auth.service;

import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;

import java.util.List;

public interface SysHtRoleService {
    List<SysHtRole> selectHtRoles(SearchSysRole searchSysRole);
}
