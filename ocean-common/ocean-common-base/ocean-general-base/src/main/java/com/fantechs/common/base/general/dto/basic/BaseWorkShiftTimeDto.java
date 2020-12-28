package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 该实体用于整理日期和班次时间，便于前端解析
 */
@Data
public class BaseWorkShiftTimeDto implements Serializable {

    /**
     * 日期-天
     */
    @ApiModelProperty(name="proLineId",value = "日期-天")
    private Long day;

    /**
     * 用于返回同一天所有的班次拼接
     */
    @ApiModelProperty(name="allWorkShift",value = "班次")
    private List<String> allWorkShift;
}
