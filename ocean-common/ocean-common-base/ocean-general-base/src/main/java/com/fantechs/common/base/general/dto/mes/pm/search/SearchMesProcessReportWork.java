package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesProcessReportWork extends BaseQuery implements Serializable {

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    /**
     * 工序代码
     */
    @ApiModelProperty(name="processCode" ,value="工序代码")
    private String processCode;
}
