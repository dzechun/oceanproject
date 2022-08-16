package com.fantechs.provider.wanbao.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Table(name = "middle_sale_order")
public class MiddleSaleOrder implements Serializable {

    @Id
    @Column(name = "sale_order_id")
    private String saleOrderId;

    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    @ApiModelProperty(name="supplierCode",value = "客户编码")
    @Column(name = "supplier_code")
    private String supplierCode;

    @ApiModelProperty(name="supplierName",value = "客户名称")
    @Column(name = "supplier_name")
    private String supplierName;

    @ApiModelProperty(name="supplierId",value = "客户id")
    @Column(name = "supplier_id")
    private String supplierId;

    @ApiModelProperty(name="orderStatus" ,value="订单状态")
    @Column(name = "order_status")
    private String orderStatus;

    @ApiModelProperty(name="orderType" ,value="订单类型")
    @Column(name = "order_type")
    private String orderType;

    @ApiModelProperty(name="factoryCode" ,value="工厂编码")
    @Column(name = "factory_code")
    private String factoryCode;

    @ApiModelProperty(name="makeOrderUserName",value = "制单人员")
    @Column(name = "make_order_user_name")
    private String makeOrderUserName;

    @ApiModelProperty(name="materialId" ,value="物料id")
    @Column(name = "material_id")
    private String materialId;

    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Column(name = "material_name")
    private String materialName;

    @ApiModelProperty(name="customerProductModel" ,value="客户型号")
    @Column(name = "customer_product_model")
    private String customerProductModel;

    @ApiModelProperty(name="customerOrderLineNumber" ,value="订单行号")
    @Column(name = "customer_order_line_number")
    private BigDecimal customerOrderLineNumber;

    @ApiModelProperty(name="salesCode" ,value="销售编码")
    @Column(name = "sales_code")
    private String salesCode;

    @ApiModelProperty(name="makeCode" ,value="制造编码")
    @Column(name = "make_code")
    private String makeCode;

    @ApiModelProperty(name="orderQty" ,value="订单数量")
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    @ApiModelProperty(name="salesCodeQty" ,value="订单数量")
    @Column(name = "sales_code_qty")
    private BigDecimal salesCodeQty;

    @ApiModelProperty(name="makeOrderDate" ,value="制单日期")
    @Column(name = "make_order_date")
    private String makeOrderDate;

    @ApiModelProperty(name="createTime" ,value="创建时间")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(name="modifiedTime" ,value="修改时间")
    @Column(name = "modified_time")
    private String modifiedTime;

    @ApiModelProperty(name="remark" ,value="修改时间")
    @Column(name = "remark")
    private String remark;

    @ApiModelProperty(name="salesDept",value = "销售部门")
    @Column(name = "sales_dept")
    private String salesDept;

    @ApiModelProperty(name="salesUserName",value = "销售人员")
    @Column(name = "sales_user_name")
    private String salesUserName;

    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Column(name = "order_date")
    private String orderDate;
}
