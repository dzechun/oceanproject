package com.fantechs.common.base.general.dto.callagv;

import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaReBarcode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CallAgvWarehouseAreaReBarcodeDto extends CallAgvWarehouseAreaReBarcode {

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "型号")
    private String productModel;

    @ApiModelProperty(name="qty",value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(name="materialFactory",value = "厂家")
    private String materialFactory;

    @ApiModelProperty(name="warehouseAreaName",value = "库区")
    private String warehouseAreaName;
}
