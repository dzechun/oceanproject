package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ProductLineLeft {

    /**
     * 当前工单
     */
    @ApiModelProperty(name="workOrderCode",value = "当前工单")
    private String workOrderCode;

    /**
     * 下一张工单
     */
    @ApiModelProperty(name="nextWorkOrderCode",value = "下一张工单")
    private String nextWorkOrderCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="materialName",value = "产品名称")
    private String materialName;

    /**
     * 产品名称
     */
    @ApiModelProperty(name="nextMaterialName",value = "下一工单产品名称")
    private String nextMaterialName;

    /**
     * 排产数量
     */
    @ApiModelProperty(name="workOrderQty",value = "排产数量")
    private Long scheduledQty;

    /**
     * 下一工单排产数量
     */
    @ApiModelProperty(name="nextWorkOrderQty",value = "下一工单排产数量")
    private Long nextscheduledQty;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    private Long workOrderQty;

    /**
     * 完成率
     */
    @ApiModelProperty(name="outputRate",value = "完成率")
    private String outputRate;

    /**
     * 投产数
     */
    @ApiModelProperty(name="productionQty",value = "上线")
    private Long productionQty;

    /**
     * 完成数量
     */
    @ApiModelProperty(name="outputQty",value = "下线")
    private Long outputQty;

    /**
     * 未投产数量
     */
    @ApiModelProperty(name="noProductionQty",value = "尚欠")
    private Long noProductionQty;
}
