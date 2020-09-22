package com.fantechs.common.base.entity.security;

import lombok.Data;

import javax.persistence.*;

@Table(name = "sys_user_role")
@Data
public class SysUserRole {
    /**
     * id
     */
    @Id
    @Column(name = "user_role_id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;
}