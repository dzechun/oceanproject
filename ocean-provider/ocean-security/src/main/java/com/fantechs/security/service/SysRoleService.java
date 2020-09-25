package com.fantechs.security.service;

import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SysRoleService  extends IService<SysRole>{

    //通过条件查询角色信息
    List<SysRole> selectRoles(SearchSysRole searchSysRole);

    //添加用户
    int addUser(Long roleId, List<Long> userIds);
}
