package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamIssue extends BaseQuery implements Serializable {

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelName",value = "产品型号")
    private String productModelName;

    /**
     * 问题编码
     */
    @ApiModelProperty(name="issueCode",value = "问题编码")
    private String issueCode;

    /**
     * 问题名称
     */
    @ApiModelProperty(name="issueName",value = "问题名称")
    private String issueName;

}
