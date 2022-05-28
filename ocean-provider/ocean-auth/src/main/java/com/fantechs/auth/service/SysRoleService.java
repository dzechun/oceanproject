package com.fantechs.auth.service;

import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SysRoleService  extends IService<SysRole>{

    //通过条件查询角色信息
    List<SysRoleDto> selectRoles(SearchSysRole searchSysRole);

    //添加用户
    int addUser(Long roleId, List<Long> userIds);

    //添加用户
    int addUserRole(Long userId, List<Long> roleIds);

    //通过用户名查询角色信息
    List<SysRoleDto> findByUserName(SearchSysRole searchSysRole);

    //通过用户id获取角色
    List<SysUserRole> findUserRoleList(Long userId);

    //
}
