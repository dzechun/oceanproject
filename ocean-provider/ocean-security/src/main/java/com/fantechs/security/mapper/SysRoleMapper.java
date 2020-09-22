package com.fantechs.security.mapper;

import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SysRoleMapper extends MyMapper<SysRole> {
    List<SysRole> findRolesByUserId(@Param(value = "userId") Long userId);

    List<SysRole> selectRoles(SearchSysRole searchSysRole);

    List<SysUser> findBindUser(String searchStr, Long roleId);

    List<SysUser> findUnBindUser(String searchStr, Long roleId);
}
