package com.fantechs.security.service;

import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    //通过条件查询用户
    List<SysUser> selectUsers(SearchSysUser searchSysUser);

    //通过人员编码精确查询
    SysUser selectByCode(String userCode);


    //用excel导入用户信息
    int importUsers(List<SysUserExcelDTO> smtUser);

}
