package com.fantechs.common.base.general.dto.mes.sfc;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 投产作业
 *
 * @author dxk
 * @version 1.0
 * @date 2021/04/25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdaCheckPackingDto implements Serializable {

    @ApiModelProperty(name = "barCode", value = "条码", required = true)
    private String barCode;
    @ApiModelProperty(name = "processId", value = "工序ID", required = true)
    private Long processId;
    @ApiModelProperty(name = "orderId", value = "工单ID", required = true)
    private Long orderId;
    @ApiModelProperty(name = "equipmentId", value = "设备ID", required = true)
    private String equipmentId;
    @ApiModelProperty(name = "operatorUserId", value = "操作人员ID", required = true)
    private Long operatorUserId;
    @ApiModelProperty(name = "stationId", value = "工位ID", required = true)
    private Long stationId;
    @ApiModelProperty(name = "checkOrNot", value = "是否检查排程", required = true)
    private Boolean checkOrNot;

}
