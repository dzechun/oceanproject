package com.fantechs.auth.mapper;


import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SysUserMapper extends MyMapper<SysUser> {

    List<SysUser> selectUsers(SearchSysUser searchSysUser);

    BaseDept selectDept(String factoryName, String deptName);

    List<String> findAllRoleId(Long userId);

    List<Long> findOrganizationList(Long userId);
}
