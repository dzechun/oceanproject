package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WanbaoBarcodeDto implements Serializable {

    /**
     * 成品条码
     */
    @ApiModelProperty(name="barcode",value = "成品条码")
    private String barcode;

    /**
     * 客户条码
     */
    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    private String customerBarcode;

    /**
     * 销售条码
     */
    @ApiModelProperty(name="salesBarcode",value = "销售条码")
    private String salesBarcode;

    private Long workOrderId;

    private Long materialId;
}
