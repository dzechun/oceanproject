package com.fantechs.common.base.general.dto.callagv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CallAgvDistributionDto {

    @ApiModelProperty(value = "周转工具（货架）ID", example = "周转工具（货架）ID")
    private Long vehicleId;

    @ApiModelProperty(value = "周转工具（货架）编码", example = "周转工具（货架）编码")
    private String vehicleCode;

    @ApiModelProperty(value = "库区ID", example = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "目标库位配送点ID", example = "目标库位配送点ID")
    private Long storageTaskPointId;

    @ApiModelProperty(value = "AGV配送类型(1-备料完成配送 2-叫料配送 3-空货架返回)", example = "AGV配送类型(1-备料完成配送 2-叫料配送 3-空货架返回)")
    private Integer type;

    @ApiModelProperty(name="agvTaskId",value = "AGV任务ID")
    private Long agvTaskId;
}
