package com.fantechs.common.base.entity.security;

import lombok.Data;

import javax.persistence.*;

@Table(name = "sys_auth_role")
@Data
public class SysAuthRole {
    /**
     * id
     */
    @Id
    @Column(name = "auth_id")
    private Long authId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id")
    private Long menuId;
}