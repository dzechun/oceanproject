package com.fantechs.common.base.general.dto.mes.pm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/20 15:59
 * @Description: 执行工序计划表搜索列表，条件封装对象
 * @Version: 1.0
 */
@Data
public class SearchMesPmExplainProcessPlanListDTO extends BaseQuery implements Serializable  {
    /**
     * 执行计划ID
     */
    @ApiModelProperty(value = "执行计划ID",example = "执行计划ID")
    @Excel(name = "执行计划ID")
    private Long explainPlanId;
    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID",example = "工单ID")
    @Excel(name = "工单ID")
    private Long workOrderId;
}
