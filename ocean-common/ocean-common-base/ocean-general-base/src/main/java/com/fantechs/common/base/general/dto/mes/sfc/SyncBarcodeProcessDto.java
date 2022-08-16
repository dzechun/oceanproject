package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SyncBarcodeProcessDto implements Serializable {

    @ApiModelProperty(name="barcodeProcessId",value = "条码过站表ID")
    private Long barcodeProcessId;

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="customerBarcode",value = "客户条码")
    private String customerBarcode;
}
