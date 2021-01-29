package com.fantechs.common.base.general.dto.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class SearchSmtBarcodeRule extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 4380956514946964883L;

    /**
     * 条码规则代码
     */
    @ApiModelProperty(name="barcodeRuleCode",value = "条码规则代码")
    private String barcodeRuleCode;

    /**
     * 条码规则名称
     */
    @ApiModelProperty(name="barcodeRuleName",value = "条码规则名称")
    private String barcodeRuleName;

    /**
     * 条码规则描述
     */
    @ApiModelProperty(name="barcodeRuleDesc",value = "条码规则描述")
    private String barcodeRuleDesc;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId" ,value="条码规则集合ID")
    private Long barcodeRuleSetId;

    /**
     * 条码规则
     */
    @ApiModelProperty(name="barcodeRule",value = "条码规则")
    private String barcodeRule;

    /**
     * 绑定状态
     */
    @ApiModelProperty(name="searchType" ,value="绑定状态 0-未绑定 1-绑定")
    private Integer searchType;
}
