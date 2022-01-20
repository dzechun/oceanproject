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
     * 条码id
     */
    @ApiModelProperty(name="barcodeId",value = "条码id")
    private Long materialBarcodeId;

    /**
     * 条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)
     */
    @ApiModelProperty(name="barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)")
    private Byte barcodeType;

    private static final long serialVersionUID = 1L;
}
