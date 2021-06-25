package com.fantechs.common.base.electronic.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchPtlClientManage extends BaseQuery implements Serializable {

    /**
     * 客户端名称
     */
    @ApiModelProperty(name="clientName",value = "客户端名称")
    private String clientName;


    /**
     * 客户端备注
     */
    @ApiModelProperty(name="clientRemark",value = "客户端备注")
    private String clientRemark;

    /**
     * 客户端类型：
     */
    @ApiModelProperty(name="clientType",value = "客户端类型：")
    private String clientType;

    /**
     * 登录密钥
     */
    @ApiModelProperty(name="secretKey",value = "登录密钥")
    private String secretKey;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    private Long orgId;
}
