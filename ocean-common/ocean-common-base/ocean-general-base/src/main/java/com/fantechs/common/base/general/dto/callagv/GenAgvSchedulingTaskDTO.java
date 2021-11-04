package com.fantechs.common.base.general.dto.callagv;

import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GenAgvSchedulingTaskDTO {

    @ApiModelProperty(name="barcode",value = "任务类型")
    private String taskTyp;

    @ApiModelProperty(name="barcode",value = "坐标列表")
    private List<String> positionCodeList;

    @ApiModelProperty(name="barcode",value = "货架编号")
    private String podCode;

    @ApiModelProperty(name="baseStorageTaskPoint",value = "起始点状态信息")
    private BaseStorageTaskPoint baseStorageTaskPoint;

    @ApiModelProperty(name="temVehicle",value = "货架绑定目的点信息")
    private TemVehicle temVehicle;
}
