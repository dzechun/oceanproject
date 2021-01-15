package com.fantechs.common.base.general.dto.mes.pm;

import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodeCollocation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2020/11/21
 */
@Data
public class SmtWorkOrderBarcodeCollocationDto extends SmtWorkOrderBarcodeCollocation implements Serializable {
    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    /**
     * 工单类型(0、量产 1、试产 2、返工 3、维修)
     */
    @Transient
    @ApiModelProperty(name="workOrderType" ,value="工单类型")
    private Integer workOrderType;

    /**
     * 物料ID
     */
    @Transient
    @ApiModelProperty(name="materialId" ,value="物料ID")
    private Long materialId;

    /**
     * 物料料号
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料料号")
    private String materialCode;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="version" ,value="版本")
    private String version;

    /**
     * 工单数量
     */
    @Transient
    @ApiModelProperty(name="workOrderQuantity" ,value="工单数量")
    private Integer workOrderQuantity;

    /**
     * 投产数量
     */
    @Transient
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    private Integer productionQuantity;

    /**
     * 工艺路线ID
     */
    @Transient
    @ApiModelProperty(name="routeId" ,value="工艺路线ID")
    private Long routeId;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    private String routeName;

    /**
     * 投入工序
     */
    @Transient
    @ApiModelProperty(name="putIntoProcess" ,value="投入工序")
    private String putIntoProcess;

    /**
     * 产出工序
     */
    @Transient
    @ApiModelProperty(name="productionProcess" ,value="产出工序")
    private String productionProcess;

    /**
     * 计划结束时间
     */
    @Transient
    @ApiModelProperty(name="plannedEndTime" ,value="计划结束时间")
    private Date plannedEndTime;

    /**
     * 订单ID
     */
    @Transient
    @ApiModelProperty(name="orderId" ,value="订单ID")
    private Long orderId;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    /**
     * 客户ID
     */
    @Transient
    @ApiModelProperty(name="customerId" ,value="客户id")
    private Long customerId;

    /**
     * 客户名称
     */
    @Transient
    @ApiModelProperty(name="customerName" ,value="客户名称")
    private String customerName;

    /**
     * 移转数量
     */
    @Transient
    @ApiModelProperty(name="transferQuantity" ,value="移转数量")
    private Integer transferQuantity;

    /**
     * 条码规则集合ID
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleId" ,value="条码规则集合ID")
    private Long barcodeRuleId;

    /**
     * 条码规则集合
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleName" ,value="条码规则集合")
    private String barcodeRuleName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
