package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StackingWorkByAutoDto implements Serializable {

    @ApiModelProperty(name="proLineId",value = "产线ID")
    private Long proLineId;

    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    @ApiModelProperty(name="stationId",value = "工位ID")
    private Long stationId;

    @ApiModelProperty(name="wanbaoBarcodeDto",value = "条码")
    private WanbaoBarcodeDto wanbaoBarcodeDto;

    @ApiModelProperty(name="stackingCode",value = "堆垛编码")
    private String stackingCode;

    @ApiModelProperty(name="stackingId",value = "堆垛ID")
    private Long stackingId;
}
