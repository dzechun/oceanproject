package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author
 * @Date 2021/12/01
 */
@Data
public class SearchProductDailyPlan extends BaseQuery {
    /**
     * 开始计划日期
     */
    @ApiModelProperty(name="planDateBegin",value = "开始计划日期")
    private String planDateBegin;

    /**
     * 结束计划日期
     */
    @ApiModelProperty(name="planDateEnd",value = "结束计划日期")
    private String planDateEnd;

    /**
     * 线别id
     */
    @ApiModelProperty(name="proLineId" ,value="线别id")
    private Long proLineId;

}
