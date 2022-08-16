package com.fantechs.common.base.general.entity.basic.search;

import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchBaseCalendar extends BaseQuery implements Serializable {

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    private Long proLineId;

    /**
     * 日期-年月
     */
    @ApiModelProperty(name="date",value = "日期-年月")
    private String date;
}
