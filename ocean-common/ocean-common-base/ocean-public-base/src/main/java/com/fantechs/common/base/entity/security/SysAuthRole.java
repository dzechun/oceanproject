package com.fantechs.common.base.entity.security;

import com.fantechs.common.base.support.ValidGroup;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "sys_auth_role")
@Data
public class SysAuthRole extends ValidGroup implements Serializable {
    private static final long serialVersionUID = 6683391438060103280L;
    /**
     * id
     */
    @Id
    @Column(name = "auth_id")
    private Long authId;

    /**
     * 角色ID
     */
    @NotNull(message = "角色Id不能为空")
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id")
    @NotNull(message = "角色Id不能为空")
    private Long menuId;
}