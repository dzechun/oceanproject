package com.fantechs.common.base.general.entity.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEsopIssue extends BaseQuery implements Serializable {

    /**
     * 产品id
     */
    @ApiModelProperty(name="materialId",value = "产品id")
    private Long materialId;

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
     * 产品型号编码
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号编码")
    private String productModelCode;

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

    /**
     * 设备IP
     */
    @ApiModelProperty(name="equipmentIp",value = "设备IP")
    private String equipmentIp;

    /**
     * 根据编码查询方式标记（传1则为等值查询）
     */
    @ApiModelProperty(name = "codeQueryMark",value = "查询方式标记")
    private Integer codeQueryMark;
}
