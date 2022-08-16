package com.fantechs.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WorkOrderBarcodeDto implements Serializable {

    @ApiModelProperty(name="barcode",value = "厂内码")
    private String barcode;

    @ApiModelProperty(name="barcodeStatus",value = "条码状态")
    private String barcodeStatus;

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;

    @ApiModelProperty(name = "cutsomerBarcode",value = "客户条码")
    private String cutsomerBarcode;
}
