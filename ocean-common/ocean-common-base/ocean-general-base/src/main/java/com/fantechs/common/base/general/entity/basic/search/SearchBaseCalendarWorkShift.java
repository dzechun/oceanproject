package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SearchBaseCalendarWorkShift extends BaseQuery implements Serializable {

    /**
     * 日历ID
     */
    @ApiModelProperty(name="calendarId",value = "日历ID")
    private Long calendarId;

    /**
     * 日期-天
     */
    @ApiModelProperty(name="proLineId",value = "日期-天")
    private Long day;
}
