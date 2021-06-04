package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesPmDailyPlan extends BaseQuery implements Serializable {

    @ApiModelProperty(name="dailyPlanId",value = "日计划ID")
    private Long dailyPlanId;

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    @ApiModelProperty(name="materialId",value = "产品料号ID")
    private Long materialId;

    @ApiModelProperty(name="proLineId",value = "线别ID")
    private Long proLineId;

    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    @ApiModelProperty(name="startTime",value = "排产计划开始时间")
    private String startTime;

    @ApiModelProperty(name="endTime",value = "排产计划结束时间")
    private String endTime;

    @ApiModelProperty(name="planTime",value = "计划时间")
    private Date planTime;
}
