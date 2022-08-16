package com.fantechs.common.base.entity.security;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "sys_user_role")
@Data
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = -2265847625865832067L;
    /**
     * id
     */
    @Id
    @Column(name = "user_role_id")
    private Long userRoleId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    @NotNull(message = "角色Id不能为空")
    private Long roleId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    @NotNull(message = "用户Id不能为空")
    private Long userId;
}