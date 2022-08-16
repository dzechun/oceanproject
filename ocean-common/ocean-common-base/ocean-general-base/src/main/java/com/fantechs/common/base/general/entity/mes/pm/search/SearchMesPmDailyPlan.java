package com.fantechs.common.base.general.entity.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
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

    @ApiModelProperty(name="planTime",value = "计划时间")
    private String planTime;

    @ApiModelProperty(name="workOrderStatus" ,value="工单状态")
    private String workOrderStatus;

    /**
     * 是否插单(0-否 1-是)
     */
    @ApiModelProperty(name="ifOrderInserting",value = "是否插单(0-否 1-是)")
    private Byte ifOrderInserting;
}
