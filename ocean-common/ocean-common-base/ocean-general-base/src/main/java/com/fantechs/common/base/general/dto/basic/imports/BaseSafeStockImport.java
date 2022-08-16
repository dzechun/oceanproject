package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BaseSafeStockImport implements Serializable {

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    @Excel(name = "仓库编码(必填)", height = 20, width = 30)
    private String warehouseCode;

    /**
     * 仓库id
     */
    @ApiModelProperty(name="warehouseId",value = "仓库id")
    private Long warehouseId;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    private Long materialId;

    /**
     * 货主编码
     */
    @ApiModelProperty(name="materialOwnerCode" ,value="货主编码")
    @Excel(name = "货主编码", height = 20, width = 30)
    private String materialOwnerCode;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    private Long materialOwnerId;

    /**
     * 最小数
     */
    @ApiModelProperty(name="minQty",value = "最小数")
    @Excel(name = "最小数", height = 20, width = 30)
    private BigDecimal minQty;

    /**
     * 最大数
     */
    @ApiModelProperty(name="maxQty",value = "最小数")
    @Excel(name = "最大数", height = 20, width = 30)
    private BigDecimal maxQty;
}
