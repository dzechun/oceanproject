package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CallAgvDistributionRestDto {

    @ApiModelProperty(name="vehicleCode",value = "货架编码")
    private String vehicleCode;

    @ApiModelProperty(name="warehouseAreaCode",value = "目的库区编码")
    private String warehouseAreaCode;

    @ApiModelProperty(name="storageCode",value = "目的库位编码")
    private String storageCode;

    @ApiModelProperty(name="type",value = "2-叫料配送 3-空货架返回")
    private Integer type;
}
