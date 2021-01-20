package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtWorkOrderBarcodePool extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 469300618938581909L;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="taskStatus" ,value="任务状态(0-待投产 1-投产中 2-已完成)")
    private Byte taskStatus;
}
