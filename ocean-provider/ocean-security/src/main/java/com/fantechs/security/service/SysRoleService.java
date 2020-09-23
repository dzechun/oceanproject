package com.fantechs.security.service;

import com.fantechs.common.base.dto.security.SysRoleExcelDTO;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SysRoleService  extends IService<SysRole>{

    //通过条件查询角色信息
    List<SysRole> selectRoles(SearchSysRole searchSysRole);

    //新增角色信息
    int insert(SysRole sysRole);

    //修改角色信息
    int updateById(SysRole sysRole);

    //删除角色信息
    int deleteByIds(List<Long> roleIds);

    // 查询已绑定角色的用户信息
    List<SysUser> findBindUser(String searchStr, Long roleId);

    // 查询未绑定角色的用户信息
    List<SysUser> findUnBindUser(String searchStr, Long roleId);

    //添加用户
    int addUser(Long roleId, List<Long> userIds);
}
