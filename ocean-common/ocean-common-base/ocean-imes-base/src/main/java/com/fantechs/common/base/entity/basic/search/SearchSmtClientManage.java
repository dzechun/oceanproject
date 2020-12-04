package com.fantechs.common.base.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSmtClientManage extends BaseQuery implements Serializable {

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
}
