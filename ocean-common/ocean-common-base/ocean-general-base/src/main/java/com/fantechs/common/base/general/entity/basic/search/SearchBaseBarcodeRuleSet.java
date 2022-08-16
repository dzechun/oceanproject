package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseBarcodeRuleSet extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 8795103778752737557L;

    /**
     * 条码规则集合编码
     */
    @ApiModelProperty(name="barcodeRuleSetCode",value = "条码规则集合编码")
    private String barcodeRuleSetCode;

    /**
     * 条码规则集合名称
     */
    @ApiModelProperty(name="barcodeRuleSetName",value = "条码规则集合名称")
    private String barcodeRuleSetName;

    /**
     * 条码规则集合描述
     */
    @ApiModelProperty(name="barcodeRuleSetDesc",value = "条码规则集合描述")
    private String barcodeRuleSetDesc;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    private Long organizationId;
}
