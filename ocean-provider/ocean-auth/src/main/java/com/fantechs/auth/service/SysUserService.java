package com.fantechs.auth.service;

import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    //通过条件查询用户
    List<SysUser> selectUsers(SearchSysUser searchSysUser);

    //通过人员编码精确查询
    SysUser selectByCode(String userCode);


    //用excel导入用户信息
    Map<String, Object> importUsers(List<SysUserExcelDTO> sysUsers);

    List<String> findAllRoleId(Long userId);

    int switchOrganization(Long organizationId);

    int updatePassword(String oldPassword, String newPassword);

    //接口保存用户
    SysUser saveByApi(SysUser sysUser);
}
