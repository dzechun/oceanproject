package com.fantechs.security.mapper;

import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SysHtUserMapper extends MyMapper<SysHtUser> {
    List<SysHtUser> selectHtUsers(SearchSysUser searchSysUser);
}
