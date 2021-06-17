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
 * 采购订单
 * om_purchase_order
 * @author 81947
 * @date 2021-06-17 10:17:21
 */
@Data
@Table(name = "om_purchase_order")
public class OmPurchaseOrder extends ValidGroup implements Serializable {
    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    @Excel(name = "采购订单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    /**
     * 采购订单编码
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    @Excel(name = "采购订单编码", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_order_code")
    private String purchaseOrderCode;

    /**
     * 订单类型
     */
    @ApiModelProperty(name="orderType",value = "订单类型")
    @Excel(name = "订单类型", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type")
    private String orderType;

    /**
     * 订单状态
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态")
    @Excel(name = "订单状态", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="") 
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 采购部门ID
     */
    @ApiModelProperty(name="purchaseDeptId",value = "采购部门ID")
    @Excel(name = "采购部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_dept_id")
    private Long purchaseDeptId;

    /**
     * 采购人员ID
     */
    @ApiModelProperty(name="purchaseUserId",value = "采购人员ID")
    @Excel(name = "采购人员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "purchase_user_id")
    private Long purchaseUserId;

    /**
     * 制单人ID
     */
    @ApiModelProperty(name="makeOrderUserId",value = "制单人ID")
    @Excel(name = "制单人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "make_order_user_id")
    private Long makeOrderUserId;

    /**
     * 制单日期
     */
    @ApiModelProperty(name="makeOrderDate",value = "制单日期")
    @Excel(name = "制单日期", height = 20, width = 30,orderNum="") 
    @Column(name = "make_order_date")
    private Date makeOrderDate;

    /**
     * 审核人ID
     */
    @ApiModelProperty(name="auditUserId",value = "审核人ID")
    @Excel(name = "审核人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核日期
     */
    @ApiModelProperty(name="auditDate",value = "审核日期")
    @Excel(name = "审核日期", height = 20, width = 30,orderNum="") 
    @Column(name = "audit_date")
    private Date auditDate;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    @Excel(name = "扩展字段1", height = 20, width = 30,orderNum="") 
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    @Excel(name = "扩展字段2", height = 20, width = 30,orderNum="") 
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    @Excel(name = "扩展字段3", height = 20, width = 30,orderNum="") 
    private String option3;

    private static final long serialVersionUID = 1L;
}