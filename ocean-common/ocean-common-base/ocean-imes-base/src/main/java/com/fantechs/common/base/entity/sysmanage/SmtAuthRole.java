package com.fantechs.common.base.entity.sysmanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by lfz on 2020/8/15.
 */
@Table(name = "smt_auth_role")
@Data
public class SmtAuthRole implements Serializable{
    private static final long serialVersionUID = 3183803811522971880L;
    /**
     * 权限ID
     */
    @Id
    @ApiModelProperty(name ="authId",value = "权限ID")
    private String authId;

    /**
     * 角色id
     */
    @ApiModelProperty(name = "roleId",value = "角色id")
    private String roleId;

    /**
     * 菜单
     */
    @ApiModelProperty(name="menuId",value = "菜单ID")
    private String menuId;

}
