package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.chi
 * @Date 2021/10/8
 */
@Data
public class ProLineBoardModel implements Serializable {
    /**
     * 排产数
     */
    @ApiModelProperty(name="scheduledQty",value = "排产数")
    private Long scheduledQty;

    /**
     * 完成数量
     */
    @ApiModelProperty(name="outputQty",value = "完成数量")
    private Long outputQty;

    /**
     * 完成率
     */
    @ApiModelProperty(name="outputRate",value = "完成率")
    private String outputRate;

    /**
     * 直通率
     */
    @ApiModelProperty(name="passRate",value = "直通率")
    private String passRate;

    /**
     * 预警良率
     */
    @ApiModelProperty(name="warningRate",value = "预警良率")
    private String warningRate;


    /**
     * 停线良率
     */
    @ApiModelProperty(name="stopProLineRate",value = "停线良率")
    private String stopProLineRate;

    /**
     * 稼动率
     */
    @ApiModelProperty(name="operationRatio",value = "稼动率")
    private String operationRatio;

    /**
     * 设备数量
     */
    @ApiModelProperty(name="equipmentQty",value = "设备数量")
    private Long equipmentQty;

    /**
     * 使用数量
     */
    @ApiModelProperty(name="useQty",value = "使用数量")
    private Long useQty;

    /**
     * 产线
     */
    @ApiModelProperty(name="proLineId",value = "产线")
    private Long proLineId;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proName",value = "产线名称")
    private String proName;

}
