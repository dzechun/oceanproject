package com.fantechs.security.mapper;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SysUserMapper extends MyMapper<SysUser> {

    List<SysUser> selectUsers(SearchSysUser searchSysUser);
}
