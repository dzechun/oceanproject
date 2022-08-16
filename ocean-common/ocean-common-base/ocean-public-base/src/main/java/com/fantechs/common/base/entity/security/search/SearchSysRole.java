package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wcz
 * @Date: 2020/8/18 15:15
 * @Description:
 * @Version: 1.0
 */
@ApiModel
@Data
public class SearchSysRole extends BaseQuery implements Serializable {
    private static final long serialVersionUID = 5038428966635384836L;
    /**
     * 角色名称
     */
    @ApiModelProperty(name="roleName" ,value="角色名称")
    private String roleName;

    /**
     * 角色描述
     */
    @ApiModelProperty(name="roleDesc" ,value="角色描述")
    private String roleDesc;

    /**
     * 用户编码或名称
     */
    @ApiModelProperty(name="searchStr" ,value="用户编码或名称")
    private  String searchStr;

    /**
     * 角色ID
     */
    @ApiModelProperty(name="roleId" ,value="角色ID")
    private  Long roleId;

    /**
     * 用户账号
     */
    @ApiModelProperty(name="userName" ,value="用户账号")
    private  String userName;

    /**
     * 是否查询已绑定角色
     */
    @ApiModelProperty(name="searchType" ,value="是否查询已绑定角色（1-查询绑定角色）")
    private  Byte searchType;

    /**
     * 用户ID
     */
    @ApiModelProperty(name="userId" ,value="用户ID")
    private Long userId;

    /**
     * 组织编码
     */
    @ApiModelProperty(name="roleCode" ,value="组织编码")
    private String roleCode;
}
