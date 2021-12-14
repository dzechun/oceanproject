package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author
 * @Date 2021/12/01
 */
@Data
public class ProductDailyPlanModel implements Serializable {

    /**
     * 生产工单
     */
    @ApiModelProperty(name="workOrderCode",value = "生产工单")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 物料名称.
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name="productModelName" ,value="产品型号")
    private String productModelName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    private String materialVersion;

    /**
     * 生产数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQty",value = "生产数量/工单数量")
    private BigDecimal workOrderQty;

    /**
     * 计划数量
     */
    @Transient
    @ApiModelProperty(name="scheduledQty",value = "计划数量")
    private BigDecimal scheduledQty;

    /**
     * 完工数量
     */
    @ApiModelProperty(name="finishedQty",value = "完工数量")
    @Transient
    private BigDecimal finishedQty;

    //

    /**
     * 投产数量
     */
    @ApiModelProperty(name="productionQty",value = "投产数量")
    @Transient
    private BigDecimal productionQty;

    /**
     * 工单状态
     */
    @Transient
    @ApiModelProperty(name="workOrderStatus" ,value="工单状态")
    private String workOrderStatus;

    /**
     * 缺料情况
     */
    @Transient
    @ApiModelProperty(name="productCondition" ,value="缺料情况")
    private String productCondition;

    /**
     * 计划变动
     */
    @Transient
    @ApiModelProperty(name="ifOrderInserting" ,value="计划变动")
    private String ifOrderInserting;

    @Transient
    @ApiModelProperty(name = "planDate",value = "计划日期（yyyy-MM-dd）")
    private String planDate;
}
