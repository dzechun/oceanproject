package com.fantechs.common.base.general.dto.daq;

import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DaqDataCollectModel implements Serializable {

    @ApiModelProperty(value = "设备信息")
    private DaqEquipment eamEquipment;

    private List<String> tableName;

    private List<String> collectDate;
}
