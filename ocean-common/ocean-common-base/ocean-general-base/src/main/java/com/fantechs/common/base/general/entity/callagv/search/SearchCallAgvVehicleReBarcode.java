package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchCallAgvVehicleReBarcode extends BaseQuery implements Serializable {

    @ApiModelProperty(name="barcodeId",value = "条码ID")
    private Long barcodeId;

    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    private Long vehicleId;

    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    @ApiModelProperty(name="barcodeIdList",value = "条码ID列表")
    private List<Long> barcodeIdList;

    @ApiModelProperty(name="productModel",value = "物料型号")
    private String productModel;

    @ApiModelProperty(name="warehouseAreaId",value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(name="groupByVehicle",value = "是否按照周转工具分组（0-否 1-是）")
    private Integer groupByVehicle;
}
