package com.fantechs.security.service;

import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 * Created by lfz on 2020/10/12.
 */
public interface SysAuthRoleService extends IService<SysAuthRole> {
    int updateBatch(List<SysAuthRole> list, Byte menuType);

    //根据角色ID和菜单ID或者授权信息
    SysAuthRole getSysAuthRole(Long roleId,Long menuId);
}
