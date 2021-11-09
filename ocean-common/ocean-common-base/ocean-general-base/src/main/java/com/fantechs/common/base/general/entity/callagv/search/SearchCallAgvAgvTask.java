package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCallAgvAgvTask extends BaseQuery implements Serializable {

    @ApiModelProperty(name="operateType",value = "任务类型")
    private Integer operateType;

    @ApiModelProperty(name="taskCode",value = "任务单头")
    private String taskCode;

    @ApiModelProperty(name="startTaskPointName",value = "起始库区")
    private String startTaskPointName;

    @ApiModelProperty(name="endTaskPointName",value = "目标库区")
    private String endTaskPointName;

    @ApiModelProperty(name="vehicleName",value = "货架")
    private String vehicleName;

    @ApiModelProperty(name="taskStatus",value = "任务状态")
    private Integer taskStatus;
}
