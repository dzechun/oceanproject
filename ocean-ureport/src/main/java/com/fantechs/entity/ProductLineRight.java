package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ProductLineRight {

    /**
     * 当前工单
     */
    @ApiModelProperty(name="workOrderCode",value = "工单")
    private String workOrderCode;

    /**
     * 工序名称
     */
    @ApiModelProperty(name="processName",value = "工序名称")
    private String processName;

    /**
     * 标准节拍
     */
    @ApiModelProperty(name="standardTime",value = "标准节拍")
    private Double standardTime;

    /**
     * 实际节拍
     */
    @ApiModelProperty(name="practicalTime",value = "实际节拍")
    private Double practicalTime;

    /**
     * 次数
     */
    @ApiModelProperty(name="numberOfTimes", value = "次数")
    private Integer numberOfTimes;

}
