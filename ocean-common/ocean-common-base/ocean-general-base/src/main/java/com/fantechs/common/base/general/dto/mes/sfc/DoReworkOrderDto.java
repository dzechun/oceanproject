package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DoReworkOrderDto implements Serializable {

    @ApiModelProperty(name = "reworkOrderCode", value = "返工单号")
    private String reworkOrderCode;

    @ApiModelProperty(name = "routeId", value = "工艺路线ID")
    private Long routeId;

    @ApiModelProperty(name = "processId", value = "工序ID")
    private Long processId;

    @ApiModelProperty(name = "barCode", value = "产品条码")
    private String barCode;

    @ApiModelProperty(name = "cartonCode", value = "包箱号")
    private String cartonCode;

    @ApiModelProperty(name = "palletCode", value = "栈板号")
    private String palletCode;

    @ApiModelProperty(name = "colorBoxCode", value = "彩盒号")
    private String colorBoxCode;

    @ApiModelProperty(name = "workOrderId", value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name = "materialId", value = "产品物料ID")
    private Long materialId;
}
