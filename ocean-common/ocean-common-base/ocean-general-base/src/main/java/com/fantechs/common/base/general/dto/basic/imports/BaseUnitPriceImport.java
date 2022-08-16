package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseUnitPriceImport implements Serializable {

    /**
     * 物料编码(必填)
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码(必填)")
    @Excel(name = "物料编码(必填)", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 工序编码(必填)
     */
    @ApiModelProperty(name="processCode" ,value="工序编码(必填)")
    @Excel(name = "工序编码(必填)", height = 20, width = 30)
    private String processCode;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 单价(必填)
     */
    @ApiModelProperty(name="unitPrice",value = "单价(必填)")
    @Excel(name = "单价(必填)", height = 20, width = 30)
    private BigDecimal unitPrice;

}
