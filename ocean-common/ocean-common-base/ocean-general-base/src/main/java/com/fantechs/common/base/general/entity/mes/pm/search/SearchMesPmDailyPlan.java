package com.fantechs.common.base.general.entity.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchMesPmDailyPlan extends BaseQuery implements Serializable {

    /**
     * 工单号
     */
    @ApiModelProperty(name="dailyPlanId" ,value="工单号")
    private String dailyPlanId;
    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

}
