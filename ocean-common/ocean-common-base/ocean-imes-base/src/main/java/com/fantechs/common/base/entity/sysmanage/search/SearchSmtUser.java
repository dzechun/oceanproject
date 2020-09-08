package com.fantechs.common.base.entity.sysmanage.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel
@Data
public class SearchSmtUser extends BaseQuery implements Serializable{
    private static final long serialVersionUID = -110480906537756636L;
    /**
     * 用户帐号
     */
    @ApiModelProperty(name="userCode" ,value="用户帐号")
    private String userCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(name="userName" ,value="用户名称")
    private String userName;

}
