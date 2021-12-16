package com.fantechs.common.base.general.dto.callagv;

import com.fantechs.common.base.general.entity.basic.BaseStorageTaskPoint;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    @ApiModelProperty(name="callAgvAgvTask",value = "货架对应的AGV任务")
    private CallAgvAgvTask callAgvAgvTask;

    @ApiModelProperty(name="map",value = "生产报告/备料计划的参数")
    private Map<String, Object> map;
}
