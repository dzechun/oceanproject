package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PDAWmsInnerDirectTransferOrderDetDto implements Serializable {


    /**
     * 扫描数量
     */
    @ApiModelProperty(name="qty",value = "扫描数量")
    private BigDecimal qty;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

     /**
     * 条码
     */
    @ApiModelProperty(name="barcodeId",value = "条码")
    private Long materialBarcodeId;

    private static final long serialVersionUID = 1L;
}
