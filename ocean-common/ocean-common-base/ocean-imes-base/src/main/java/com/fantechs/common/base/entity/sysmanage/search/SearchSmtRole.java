package com.fantechs.common.base.entity.sysmanage.search;

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
public class SearchSmtRole  extends BaseQuery implements Serializable {

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
}
