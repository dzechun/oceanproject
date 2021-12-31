package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
//    @Excel(name = "客户ID", height = 20, width = 30)
    @Column(name = "sales_order_id")
    private Long salesOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    //@Excel(name = "核心系统单据类型编码", height = 20, width = 30,orderNum="1")
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    //@Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="1")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    //@Excel(name = "系统单据类型编码", height = 20, width = 30,orderNum="1")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 销售订单号
     */
    @ApiModelProperty(name="salesOrderCode",value = "销售订单号")
    @Excel(name = "销售订单号", height = 20, width = 30, orderNum = "0")
    @Column(name = "sales_order_code")
    private String salesOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30, orderNum = "4")
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
//    @Excel(name = "客户ID", height = 20, width = 30)
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 客户订单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户订单号")
    @Excel(name = "客户订单号", height = 20, width = 30, orderNum = "3")
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    /**
     * 条码规则集合ID
     */
    @ApiModelProperty(name="barcodeRuleSetId",value = "条码规则集合ID")
//    @Excel(name = "条码规则集合ID", height = 20, width = 30)
    @Column(name = "barcode_rule_set_id")
    private Long barcodeRuleSetId;

    /**
     * 交货方式
     */
    @ApiModelProperty(name="deliveryType",value = "交货方式")
    @Excel(name = "交货方式", height = 20, width = 30, orderNum = "6")
    @Column(name = "delivery_type")
    private String deliveryType;

    /**
     * 销售部门ID
     */
    @ApiModelProperty(name="salesDeptId",value = "销售部门")
    @Column(name = "sales_dept_id")
    private Long salesDeptId;

    /**
     * 销售人员ID
     */
    @ApiModelProperty(name="salesUserId",value = "销售人员ID")
    @Column(name = "sales_user_id")
    private Long salesUserId;

    /**
     * 数据来源
     */
    @ApiModelProperty(name="dataSource",value = "数据来源")
    @Excel(name = "数据来源", height = 20, width = 30, orderNum = "9")
    @Column(name = "data_source")
    private String dataSource;

    /**
     * 订单类型
     */
    @ApiModelProperty(name="salesOrderType",value = "订单类型")
    @Excel(name = "订单类型", height = 20, width = 30, orderNum = "10")
    @Column(name = "sales_order_type")
    private String salesOrderType;

    /**
     * 订单状态
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态")
//    @Excel(name = "订单状态", height = 20, width = 30)
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30, orderNum = "1")
    private Byte status;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30, orderNum = "11")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 制单人员ID
     */
    @ApiModelProperty(name="makeOrderUserId",value = "制单人员ID")
    @Excel(name = "制单人员ID", height = 20, width = 30, orderNum = "12")
    @Column(name = "make_order_user_id")
    private Long makeOrderUserId;

    /**
     * 制单日期
     */
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Excel(name = "制单日期", height = 20, width = 30, orderNum = "13")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    @Column(name = "make_order_date")
    private Date makeOrderDate;

    /**
     * 审核人员ID
     */
    @ApiModelProperty(name="auditUserId",value = "审核人员ID")
    @Excel(name = "审核人员ID", height = 20, width = 30, orderNum = "14")
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核日期
     */
    @ApiModelProperty(name="auditDate",value = "审核日期")
    @Excel(name = "审核日期", height = 20, width = 30, orderNum = "15")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    @Column(name = "audit_date")
    private Date auditDate;


    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30, orderNum = "17")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
//    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
//    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss", orderNum = "19")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
//    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss", orderNum = "21")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
//    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
//    @Excel(name = "扩展字段1", height = 20, width = 30)
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
//    @Excel(name = "扩展字段2", height = 20, width = 30)
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
//    @Excel(name = "扩展字段3", height = 20, width = 30)
    private String option3;

    private static final long serialVersionUID = 1L;
}