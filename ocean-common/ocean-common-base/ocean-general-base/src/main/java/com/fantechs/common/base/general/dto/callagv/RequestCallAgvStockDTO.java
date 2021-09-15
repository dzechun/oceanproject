package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RequestCallAgvStockDTO {

    @ApiModelProperty(name="barcodeIdList",value = "条码ID列表")
    private List<Long> barcodeIdList;

    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    private Long vehicleId;

    @ApiModelProperty(name="storageTaskPointId",value = "配送点ID")
    private Long storageTaskPointId;
}
