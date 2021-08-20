package com.fantechs.common.base.general.dto.daq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DaqDataCollectModel implements Serializable {

    @ApiModelProperty(value = "设备信息")
    private List<String> tableName;

    @ApiModelProperty(value = "设备信息")
    private Map<String, Object> data;
}
