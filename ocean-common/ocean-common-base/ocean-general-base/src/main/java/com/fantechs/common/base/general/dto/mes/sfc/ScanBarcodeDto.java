package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanBarcodeDto implements Serializable {

    @ApiModelProperty(name = "barCode", value = "条码", required = true)
    private String barCode;
    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
    @ApiModelProperty(name = "processId", value = "工序ID", required = true)
    private Long processId;
    @ApiModelProperty(name = "proLineId", value = "产线ID", required = true)
    private Long proLineId;
    @ApiModelProperty(name = "ip", value = "ip", required = true)
    private String ip;
    @ApiModelProperty(name = "type", value = "类型1-包箱，2-栈板，3-出库", required = true)
    private String type;
}
