package com.fantechs.auth.service;

import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;

import java.util.List;

public interface SysHtUserService {
    List<SysHtUser> selectHtUsers(SearchSysUser searchSysUser);
}
