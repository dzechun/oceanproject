package com.fantechs.common.base.agv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AgvCallBackDTO {

    @ApiModelProperty(value = "请求编号", example = "请求编号")
    private String reqCode;

    @ApiModelProperty(value = "请求时间戳", example = "yyyy-MM-dd HH:mm:ss")
    private String reqTime;

    @ApiModelProperty(value = "接口名称(agvCallback)", example = "TCP协议必传，REST协议不用传")
    private String interfaceName;

    @ApiModelProperty(value = "地码 X 坐标(mm)", example = "地码 X 坐标(mm)")
    private String cooX;

    @ApiModelProperty(value = "地码 Y 坐标(mm)", example = "地码 Y 坐标(mm)")
    private String cooY;

    @ApiModelProperty(value = "当前位置编号", example = "当前位置编号")
    private String currentPositionCode;

    @ApiModelProperty(value = "自定义字段", example = "不超过2000个字符")
    private String data;

    @ApiModelProperty(value = "地图编号", example = "地图编号")
    private String mapCode;

    @ApiModelProperty(value = "地码编号：任务完成时有值", example = "地码编号：任务完成时有值")
    private String mapDataCode;

    @ApiModelProperty(value = "方法名(start : 任务开始, outbin : 走出储位, end : 任务结束)", example = "方法名(start : 任务开始, outbin : 走出储位, end : 任务结束)")
    private String method;

    @ApiModelProperty(value = "货架编号", example = "货架编号")
    private String podCode;

    @ApiModelProperty(value = "地图方向", example = "180,0,90,-90 分别对应地图的左,右,上,下")
    private String podDir;

    @ApiModelProperty(value = "AGV编号", example = "AGV编号")
    private String robotCode;

    @ApiModelProperty(value = "当前任务单号", example = "当前任务单号")
    private String taskCode;

    @ApiModelProperty(value = "工作位", example = "工作位")
    private String wbCode;
}
