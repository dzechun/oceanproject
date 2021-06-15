package com.fantechs.common.base.general.dto.restapi;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapProLineApi extends BaseQuery implements Serializable {

    /**
     * 开始时间
     */
    @ApiModelProperty(name="startTime" ,value="开始时间(yyyyMMdd)")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="endTime" ,value="结束时间(yyyyMMdd)")
    private String endTime;
}
