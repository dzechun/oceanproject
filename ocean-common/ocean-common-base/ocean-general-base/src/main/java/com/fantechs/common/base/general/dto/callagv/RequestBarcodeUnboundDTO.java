package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RequestBarcodeUnboundDTO {

    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    private Long vehicleId;

    @ApiModelProperty(name="vehicleReBarcodeIdList",value = "周转工具条码ID列表")
    private List<Long> vehicleReBarcodeIdList;

    @ApiModelProperty(name = "是否已经全部解绑(0-否 1-是)", value = "是否已经全部解绑(0-否 1-是)")
    private Integer type;
}
