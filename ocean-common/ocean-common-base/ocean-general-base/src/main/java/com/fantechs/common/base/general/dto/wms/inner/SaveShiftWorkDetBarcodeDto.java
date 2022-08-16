package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveShiftWorkDetBarcodeDto implements Serializable {



    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;
}
