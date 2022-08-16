package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 销售订单履历表
 * om_ht_sales_order
 * @author Law
 * @date 2021-04-21 14:55:37
 */
@Data
@Table(name = "om_ht_sales_order")
public class OmHtSalesOrder extends ValidGroup implements Serializable {
    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="htSalesOrderId",value = "销售订单ID")
//    @Excel(name = "销售订单ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_sales_order_id")
    private Long htSalesOrderId;

    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
//    @Excel(name = "销售订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "sales_order_id")
    private Long salesOrderId;

    /**
     * 销售订单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
//    @Excel(name = "销售订单号", height = 20, width = 30,orderNum="")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
//    @Excel(name = "合同号", height = 20, width = 30,orderNum="")
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
//    @Excel(name = "客户ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 客户订单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户订单号")
//    @Excel(name = "客户订单号", height = 20, width = 30,orderNum="")
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    /**
     * 来源单据号
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据号")
//    @Excel(name = "来源单据号", height = 20, width = 30,orderNum="")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 来源单据行号
     */
    @ApiModelProperty(name="sourceOrderLineNumber",value = "来源单据行号")
//    @Excel(name = "来源单据行号", height = 20, width = 30,orderNum="")
    @Column(name = "source_order_line_number")
    private String sourceOrderLineNumber;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
//    @Excel(name = "条码规则集合ID", height = 20, width = 30,orderNum="")
    @Column(name = "barcode_rule_set_id")
    private Long barcodeRuleSetId;

    /**
     * 交货方式
     */
    @ApiModelProperty(name="deliveryType",value = "交货方式")
//    @Excel(name = "交货方式", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_type")
    private String deliveryType;

    /**
     * 销售部门
     */
    @ApiModelProperty(name="salesDept",value = "销售部门")
//    @Excel(name = "销售部门", height = 20, width = 30,orderNum="")
    @Column(name = "sales_dept")
    private String salesDept;

    /**
     * 销售人员
     */
    @ApiModelProperty(name="salesUserName",value = "销售人员")
//    @Excel(name = "销售人员", height = 20, width = 30,orderNum="")
    @Column(name = "sales_user_name")
    private String salesUserName;

    /**
     * 数据来源
     */
    @ApiModelProperty(name="dataSource",value = "数据来源")
//    @Excel(name = "数据来源", height = 20, width = 30,orderNum="")
    @Column(name = "data_source")
    private String dataSource;

    /**
     * 订单类型
     */
    @ApiModelProperty(name="orderType",value = "订单类型")
//    @Excel(name = "订单类型", height = 20, width = 30,orderNum="")
    @Column(name = "order_type")
    private String orderType;

    /**
     * 订单状态
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态")
//    @Excel(name = "订单状态", height = 20, width = 30,orderNum="")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
//    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
//    @Excel(name = "订单日期", height = 20, width = 30,orderNum="")
    @Column(name = "order_date")
    private String orderDate;

    /**
     * 制单人员
     */
    @ApiModelProperty(name="makeOrderUserName",value = "制单人员")
//    @Excel(name = "制单人员", height = 20, width = 30,orderNum="")
    @Column(name = "make_order_user_name")
    private String makeOrderUserName;

    /**
     * 制单日期
     */
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
//    @Excel(name = "制单日期", height = 20, width = 30,orderNum="")
    @Column(name = "make_order_date")
    private String makeOrderDate;

    /**
     * 审核人员
     */
    @ApiModelProperty(name="auditUserName",value = "审核人员")
//    @Excel(name = "审核人员", height = 20, width = 30,orderNum="")
    @Column(name = "audit_user_name")
    private String auditUserName;

    /**
     * 审核日期
     */
    @ApiModelProperty(name="auditDate",value = "审核日期")
//    @Excel(name = "审核日期", height = 20, width = 30,orderNum="")
    @Column(name = "audit_date")
    private String auditDate;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
//    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
//    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
//    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
//    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private String createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
//    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
//    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private String modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
//    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
//    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
//    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
//    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="")
    private String option3;

    private static final long serialVersionUID = 1L;
}