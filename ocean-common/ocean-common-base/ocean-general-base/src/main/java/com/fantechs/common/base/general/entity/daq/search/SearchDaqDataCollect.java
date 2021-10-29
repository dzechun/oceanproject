package com.fantechs.common.base.general.entity.daq.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchDaqDataCollect extends BaseQuery implements Serializable {

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    private String equipmentCode;

    /**
     * 采集时间
     */
    @ApiModelProperty(name="startTime" ,value="采集时间(YYYY-MM-DD)")
    private String collectTime;
}
