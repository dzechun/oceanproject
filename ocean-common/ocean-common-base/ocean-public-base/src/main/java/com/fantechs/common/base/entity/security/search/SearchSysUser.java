package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchSysUser extends BaseQuery implements Serializable {
    private static final long serialVersionUID = -110480906537756636L;
    /**
     * 用户帐号
     */
    @ApiModelProperty(name="userCode" ,value="用户帐号")
    private String userCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(name="nickName" ,value="用户名称")
    private String nickName;

    /**
     * 角色ID
     */
    @ApiModelProperty(name="roleId" ,value="角色id")
    private Long roleId;

    /**
     * 角色ID
     */
    @ApiModelProperty(name="searchType" ,value="绑定状态 0-未绑定 1-绑定")
    private Integer searchType;
}
