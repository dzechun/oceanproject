package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CommitInnerStockBarcodeDto implements Serializable {

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号 5-非系统条码)
     */
    @ApiModelProperty(name="barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号 5-非系统条码)")
    private Byte barcodeType;

    /**
     * 批号
     */
    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionTime",value = "生产日期")
    private String productionTime;

    /**
     * 条码物料数量
     */
    @ApiModelProperty(name="materialQty",value = "条码物料数量")
    private BigDecimal materialQty;

}