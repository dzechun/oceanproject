package com.fantechs.security.service;

import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;

import java.util.List;

public interface SysUserService {
    //查询所有有效的用户
    List<SysUser> selectAllUsers();

    //通过条件查询用户
    List<SysUser> selectUsers(SearchSysUser searchSysUser);

    //通过用户ID查询用户
    SysUser selectById(String userId);

    //通过人员编码精确查询
    SysUser selectByCode(String userCode);

    //新增用户信息
    int insert(SysUser sysUser);

    //修改用户信息
    int updateById(SysUser sysUser);

    //删除用户信息
    int delUser(List<String> userIds);

    //通过查询条件导出用户信息
    List<SysUserExcelDTO> selectUsersExcelDto(SearchSysUser searchSysUser);

    //用excel导入用户信息
    int importUsers(List<SysUser> smtUser);

}
