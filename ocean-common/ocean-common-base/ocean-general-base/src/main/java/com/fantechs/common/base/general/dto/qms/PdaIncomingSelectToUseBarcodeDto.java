package com.fantechs.common.base.general.dto.qms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @date 2020-12-28 15:15:07
 */
@Data
public class PdaIncomingSelectToUseBarcodeDto implements Serializable {

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

}
