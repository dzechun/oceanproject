package com.fantechs.common.base.general.entity.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 采购订单
 * om_purchase_order
 * @author 86178
 * @date 2021-09-08 17:46:11
 */
@Data
@Table(name = "om_purchase_order")
public class OmPurchaseOrder extends ValidGroup implements Serializable {
    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    @Id
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    @Excel(name = "采购订单编码", height = 20, width = 30,orderNum="1")
    @Column(name = "purchase_order_code")
    private String purchaseOrderCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 订单类型
     */
    @ApiModelProperty(name="orderType",value = "订单类型")
    @Column(name = "order_type")
    private String orderType;

    /**
     * 订单状态（1-开立 2-审核）
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态（1-开立 2-审核）")
    @Excel(name = "订单状态（1-开立 2-审核）", height = 20, width = 30,orderNum="7")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;


    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 采购部门ID
     */
    @ApiModelProperty(name="purchaseDeptId",value = "采购部门ID")
    @Column(name = "purchase_dept_id")
    private Long purchaseDeptId;

    /**
     * 采购人员ID
     */
    @ApiModelProperty(name="purchaseUserId",value = "采购人员ID")
    @Column(name = "purchase_user_id")
    private Long purchaseUserId;

    /**
     * 制单人ID
     */
    @ApiModelProperty(name="makeOrderUserId",value = "制单人ID")
    @Column(name = "make_order_user_id")
    private Long makeOrderUserId;

    /**
     * 制单日期
     */
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Column(name = "make_order_date")
    private Date makeOrderDate;

    /**
     * 审核人ID
     */
    @ApiModelProperty(name="auditUserId",value = "审核人ID")
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核日期
     */
    @ApiModelProperty(name="auditDate",value = "审核日期")
    @Column(name = "audit_date")
    private Date auditDate;

    /**
     * 项目类别名称
     */
    @ApiModelProperty(name="itemCategoryName",value = "项目类别名称")
    @Column(name = "item_category_name")
    private String itemCategoryName;

    /**
     * 订单单位名称
     */
    @ApiModelProperty(name="orderUnitName",value = "订单单位名称")
    @Column(name = "order_unit_name")
    private String orderUnitName;

    /**
     * 库存地点
     */
    @ApiModelProperty(name="inventorySite",value = "库存地点")
    @Column(name = "inventory_site")
    private String inventorySite;

    /**
     * 免费项目
     */
    @ApiModelProperty(name="freeItem",value = "免费项目")
    @Column(name = "free_item")
    private String freeItem;

    /**
     * 退货项目
     */
    @ApiModelProperty(name="salesReturnItem",value = "退货项目")
    @Column(name = "sales_return_item")
    private String salesReturnItem;

    /**
     * 币别ID
     */
    @ApiModelProperty(name="currencyId",value = "币别ID")
    @Column(name = "currency_id")
    private Long currencyId;

    /**
     * 计划交货日期
     */
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Column(name = "plan_delivery_date")
    private Date planDeliveryDate;

    /**
     * 审核状态(1-未审核 2-已审核)
     */
    @ApiModelProperty(name="auditStatus",value = "审核状态(1-未审核 2-已审核)")
    @Column(name = "audit_status")
    private Byte auditStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="6")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    @Transient
    @ApiModelProperty(name="omPurchaseOrderDetList",value = "采购单明细")
    private List<OmPurchaseOrderDet> omPurchaseOrderDetList;

    private static final long serialVersionUID = 1L;
}