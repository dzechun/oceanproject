package com.fantechs.common.base.entity.apply.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
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
}
