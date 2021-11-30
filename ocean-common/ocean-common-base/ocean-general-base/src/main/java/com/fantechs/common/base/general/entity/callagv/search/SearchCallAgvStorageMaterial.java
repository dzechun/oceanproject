package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCallAgvStorageMaterial extends BaseQuery implements Serializable {

    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    @ApiModelProperty(name="warehouseAreaName",value = "库区")
    private String warehouseAreaName;

    @ApiModelProperty(name="storageCode",value = "库位")
    private String storageCode;

    @ApiModelProperty(name="storageType",value = "库位类型（1-存货 2-收货 3-发货）")
    private Byte storageType;

    @ApiModelProperty(name="vehicleCode",value = "货架")
    private String vehicleCode;

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    @ApiModelProperty(name="productModel",value = "物料型号")
    private String productModel;

    @ApiModelProperty(name="batch",value = "批次")
    private String batch;

    @ApiModelProperty(name="batchCode",value = "批号")
    private String batchCode;

    @ApiModelProperty(name="sunmmary",value = "是否汇总(0-否 1-是)")
    private Integer summary;
}
