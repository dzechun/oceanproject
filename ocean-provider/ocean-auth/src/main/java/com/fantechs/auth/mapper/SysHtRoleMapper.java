package com.fantechs.auth.mapper;

import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SysHtRoleMapper extends MyMapper<SysHtRole> {
    List<SysHtRole> selectHtRoles(SearchSysRole searchSysRole);
}