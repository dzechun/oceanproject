package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CallAgvVehicleBarcodeDTO {

    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    private Long vehicleId;

    @ApiModelProperty(name="vehicleCode",value = "周转工具编码")
    private String vehicleCode;

    @ApiModelProperty(name="vehicleName",value = "周转工具名称")
    private String vehicleName;

    @ApiModelProperty(name="货架关联物料条码列表",value = "货架关联物料条码列表")
    private List<CallAgvVehicleReBarcodeDto> callAgvVehicleReBarcodeDtoList;
}
