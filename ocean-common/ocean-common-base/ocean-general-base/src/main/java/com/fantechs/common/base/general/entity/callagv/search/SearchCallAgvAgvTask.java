package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchCallAgvAgvTask extends BaseQuery implements Serializable {

    @ApiModelProperty(name="operateType",value = "任务类型")
    private Integer operateType;

    @ApiModelProperty(name="taskCode",value = "任务单头")
    private String taskCode;

    @ApiModelProperty(name="startTaskPointName",value = "起始配送点")
    private String startTaskPointName;

    @ApiModelProperty(name="endStorageTaskPointId",value = "目标配送点ID")
    private Long endStorageTaskPointId;

    @ApiModelProperty(name="endTaskPointName",value = "目标配送点")
    private String endTaskPointName;

    @ApiModelProperty(name="inWarehouseAreaName",value = "起始库区")
    private String inWarehouseAreaName;

    @ApiModelProperty(name="outWarehouseAreaName",value = "目标库区")
    private String outWarehouseAreaName;

    @ApiModelProperty(name="vehicleId",value = "货架ID")
    private Long vehicleId;

    @ApiModelProperty(name="vehicleName",value = "货架")
    private String vehicleName;

    @ApiModelProperty(name="taskStatus",value = "任务状态")
    private Integer taskStatus;

    @ApiModelProperty(name="userID",value = "用户ID")
    private Long userId;

    @ApiModelProperty(name="createTime" ,value="创建时间(YYYY-MM-DD)")
    private String createTime;

    @ApiModelProperty(name="productModel",value = "物料型号")
    private String productModel;

    @ApiModelProperty(name="ifToday",value = "是否只查当天的数据(0-否 1-是)")
    private Integer ifToday;

    @ApiModelProperty(name="taskStatusList",value = "任务状态列表")
    private List<Integer> taskStatusList;
}
