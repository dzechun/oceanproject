package com.fantechs.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ProductLineTop {

    /**
     * 排产数
     */
    @ApiModelProperty(name="scheduledQty",value = "当日目标产量")
    private Long scheduledQty;

    /**
     * 完成数量
     */
    @ApiModelProperty(name="outputQty",value = "当日累计完成")
    private Long outputQty;

    /**
     * 未完成数量
     */
    @ApiModelProperty(name="noOutputQty",value = "当日累计尚欠")
    private Long noOutputQty;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private String workOrderId;
}
