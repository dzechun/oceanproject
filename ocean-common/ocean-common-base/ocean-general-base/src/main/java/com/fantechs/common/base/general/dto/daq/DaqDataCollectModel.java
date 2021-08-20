package com.fantechs.common.base.general.dto.daq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DaqDataCollectModel implements Serializable {

    @ApiModelProperty(value = "")
    private List<String> tableName;

    @ApiModelProperty(value = "")
    private List<DaqDataCollectDto> daqDataCollectDtos;
}
