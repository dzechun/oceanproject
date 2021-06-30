package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CheckShiftWorkBarcodeRecordDto implements Serializable {

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 包装单位名称
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位名称")
    private String packingUnitName;

    /**
     * 包装规格
     */
    @ApiModelProperty(name="packageSpecificationQuantity",value = "包装规格")
    private BigDecimal packageSpecificationQuantity;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    private BigDecimal planQty;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQty",value = "物料数量")
    private BigDecimal materialQty;
}
