package com.fantechs.provider.wanbao.api.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "middle_out_delivery_order")
public class MiddleOutDeliveryOrder implements Serializable {

    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    @Id
    @Column(name = "delivery_order_id")
    private String deliveryOrderId;

    @ApiModelProperty(name="deliveryOrderCode",value = "销售订单号")
    @Column(name = "delivery_order_code")
    private String deliveryOrderCode;

    @ApiModelProperty(name="planDespatchDate",value = "计划出货日期")
    @Column(name = "plan_despatch_date")
    private String planDespatchDate;

    @ApiModelProperty(name="actualDespatchDate",value = "实际出货日期")
    @Column(name = "actual_despatch_date")
    private String actualDespatchDate;

    @ApiModelProperty(name="customerCode" ,value="客户编码")
    @Column(name = "customer_code")
    private String customerCode;

    @ApiModelProperty(name="customerName" ,value="客户名称")
    @Column(name = "customer_name")
    private String customerName;

    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    @ApiModelProperty(name="auditStatus" ,value="审批状态")
    @Column(name = "audit_status")
    private String auditStatus;

    @ApiModelProperty(name="orderStatus",value = "订单状态")
    @Column(name = "order_status")
    private String orderStatus;

    @ApiModelProperty(name="materialId" ,value="物料id")
    @Column(name = "material_id")
    private String materialId;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Column(name = "material_name")
    private String materialName;

    @ApiModelProperty(name="salesOrderCode" ,value="销售订单")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    @ApiModelProperty(name="customerOrderCode" ,value="订单行号")
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    @ApiModelProperty(name="productModelCode" ,value="销售编码")
    @Column(name = "product_model_code")
    private String productModelCode;

    @ApiModelProperty(name="salesOrderNo" ,value="销售单号")
    @Column(name = "sales_order_no")
    private String salesOrderNo;

    @ApiModelProperty(name="outOrderCode" ,value="出库单号")
    @Column(name = "out_order_code")
    private String outOrderCode;

    /**
     * 柜号
     */
    @ApiModelProperty(name="containerNumber",value = "柜号")
    @Column(name = "container_number")
    private String containerNumber;

    /**
     * 报关地点
     */
    @ApiModelProperty(name="declarationLocation ",value = "报关地点")
    @Column(name = "declaration_location")
    private String declarationLocation;

    /**
     * 起运港
     */
    @ApiModelProperty(name="portFrom",value = "起运港")
    @Column(name = "port_from")
    private String portFrom;

    /**
     * 业务员
     */
    @ApiModelProperty(name="salesName",value = "业务员")
    @Column(name = "sales_name")
    private String salesName;

    @ApiModelProperty(name="packingQty" ,value="包装数量")
    @Column(name = "packing_qty")
    private BigDecimal packingQty;

    @ApiModelProperty(name="dispatchQty" ,value="订单数量")
    @Column(name = "dispatch_qty")
    private BigDecimal dispatchQty;

    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Column(name = "order_date")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Column(name = "sales_code")
    private String salesCode;

    @ApiModelProperty(name="relatedOrderCode1",value = "相关单号")
    @Column(name = "related_order_code_1")
    private String relatedOrderCode1;

    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Column(name = "option1")
    private String option1;

    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Column(name = "option2")
    private String option2;

    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Column(name = "option3")
    private String option3;

    @ApiModelProperty(name="option4",value = "扩展字段4")
    @Column(name = "option4")
    private String option4;

    @ApiModelProperty(name="option5",value = "扩展字段5")
    @Column(name = "option5")
    private String option5;

    @ApiModelProperty(name="option6",value = "扩展字段6")
    @Column(name = "option6")
    private String option6;

    @ApiModelProperty(name="option7",value = "扩展字段7")
    @Column(name = "option7")
    private String option7;

    @ApiModelProperty(name="option8",value = "扩展字段8")
    @Column(name = "option8")
    private String option8;

    @ApiModelProperty(name="option9",value = "扩展字段9")
    @Column(name = "option9")
    private String option9;

    @ApiModelProperty(name="option10",value = "扩展字段10")
    @Column(name = "option10")
    private String option10;

    @ApiModelProperty(name="option11",value = "扩展字段11")
    @Column(name = "option11")
    private String option11;

    @ApiModelProperty(name="option12",value = "扩展字段12")
    @Column(name = "option12")
    private String option12;

    @ApiModelProperty(name="option13",value = "扩展字段13")
    @Column(name = "option13")
    private String option13;

    @ApiModelProperty(name="option14",value = "扩展字段14")
    @Column(name = "option14")
    private String option14;

}
