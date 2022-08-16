package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2020/9/3.
 */
@Data
public class SearchSysMenuInfo extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 7758627630285512091L;
    /**
     * 菜单编码
     */
    @ApiModelProperty(name = "menuCode",value = "菜单编码")
    private String menuCode;

    /**
     * 菜单名称
     */
    @ApiModelProperty(name = "menuName",value = "菜单名称")
    private String menuName;

    /**
     * 菜单路径
     */
    @ApiModelProperty(name = "premenuId",value = "菜单路径")
    private String premenuId;

    /**
     * 父级ID
     */
    @ApiModelProperty(name = "parentId",value = "菜单编码")
    private Integer parentId;

    /**
     * 菜单类型
     */
    @ApiModelProperty(name = "menuType",value = "菜单类型")
    private Integer menuType;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "roleId",value = "菜单编码")
    private Integer roleId;
}
