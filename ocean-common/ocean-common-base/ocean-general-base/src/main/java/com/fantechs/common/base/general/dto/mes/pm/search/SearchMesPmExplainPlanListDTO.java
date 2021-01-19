package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 执行计划（周/日计划表）搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesPmExplainPlanListDTO extends BaseQuery implements Serializable  {
    @ApiModelProperty(value = "计划开工时间",example = "计划开工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    private java.util.Date planedStartDate;
    @ApiModelProperty(value = "产线ID",example = "产线ID")
    private Long proLineId;
    @ApiModelProperty(value = "工单状态",example = "工单状态")
    private Byte workOrderStatus;
    @ApiModelProperty(value = "工单编码",example = "工单编码")
    private String workOrderCode;
}