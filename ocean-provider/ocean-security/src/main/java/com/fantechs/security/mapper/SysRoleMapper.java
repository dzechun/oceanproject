package com.fantechs.security.mapper;

import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends MyMapper<SysRole> {
    List<SysRole> findRolesByUserId(@Param(value = "userId") Long userId);

    List<SysRoleDto> selectRoles(SearchSysRole searchSysRole);

//    List<SysUser> findBindUser(Map<String,Object> map);

    List<SysRoleDto> findMenuRoles();

    List<SysRoleDto> findByUserName(SearchSysRole searchSysRole);
}
