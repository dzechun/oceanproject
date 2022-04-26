package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SyncWorkOrderBarcodeDto implements Serializable {

    @ApiModelProperty(name="barcodeStatus",value = "条码状态(0-待投产 1-投产中 2-已完成 3-待打印)")
    private Byte barcodeStatus;

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

}
