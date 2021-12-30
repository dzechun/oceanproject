package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BarcodeWarehouseAreaDto {

    @ApiModelProperty(name="barcodeIdList",value = "条码ID列表")
    private List<Long> barcodeIdList;

    @ApiModelProperty(name="warehouseAreaId",value = "库区ID")
    private Long warehouseAreaId;
}
