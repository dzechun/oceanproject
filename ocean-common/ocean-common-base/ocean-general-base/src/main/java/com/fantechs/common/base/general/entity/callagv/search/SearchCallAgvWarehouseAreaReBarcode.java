package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCallAgvWarehouseAreaReBarcode extends BaseQuery implements Serializable {

    @ApiModelProperty(name="barcodeId",value = "条码ID")
    private Long barcodeId;

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "物料型号")
    private String productModel;

    @ApiModelProperty(name="warehouseAreaId",value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(name="warehouseAreaName",value = "库区")
    private String warehouseAreaName;
}
