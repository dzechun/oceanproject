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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 采购订单明细
 * om_purchase_order_det
 * @author 86178
 * @date 2021-09-08 17:39:05
 */
@Data
@Table(name = "om_purchase_order_det")
public class OmPurchaseOrderDet extends ValidGroup implements Serializable {
    /**
     * 采购订单明细ID
     */
    @ApiModelProperty(name="purchaseOrderDetId",value = "采购订单明细ID")
    @Excel(name = "采购订单明细ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "purchase_order_det_id")
    private Long purchaseOrderDetId;

    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    @Excel(name = "采购订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    /**
     * 项目号
     */
    @ApiModelProperty(name="projectCode",value = "项目号")
    @Excel(name = "项目号", height = 20, width = 30,orderNum="")
    @Column(name = "project_code")
    private String projectCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="")
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="")
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 交货数量
     */
    @ApiModelProperty(name="actualQty",value = "交货数量")
    @Excel(name = "交货数量", height = 20, width = 30,orderNum="")
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 工厂ID
     */
    @ApiModelProperty(name="factoryId",value = "工厂ID")
    @Excel(name = "工厂ID", height = 20, width = 30,orderNum="")
    @Column(name = "factory_id")
    private Long factoryId;

    /**
     * 交货日期
     */
    @ApiModelProperty(name="deliveryDate",value = "交货日期")
    @Excel(name = "交货日期", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_date")
    private Date deliveryDate;

    /**
     * 单价(元)
     */
    @ApiModelProperty(name="unitPrice",value = "单价(元)")
    @Excel(name = "单价(元)", height = 20, width = 30,orderNum="")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 该物料总金额
     */
    @ApiModelProperty(name="materialTotalPrice",value = "该物料总金额")
    @Excel(name = "该物料总金额", height = 20, width = 30,orderNum="")
    @Column(name = "material_total_price")
    private BigDecimal materialTotalPrice;

    /**
     * 累计收货数量
     */
    @ApiModelProperty(name="totalReceivingQty",value = "累计收货数量")
    @Excel(name = "累计收货数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_receiving_qty")
    private BigDecimal totalReceivingQty;

    /**
     * 累计退货数量
     */
    @ApiModelProperty(name="totalSalesReturnQty",value = "累计退货数量")
    @Excel(name = "累计退货数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_sales_return_qty")
    private BigDecimal totalSalesReturnQty;

    /**
     * 累计计划送货数量
     */
    @ApiModelProperty(name="totalPlanDeliveryQty",value = "累计计划送货数量")
    @Excel(name = "累计计划送货数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_plan_delivery_qty")
    private BigDecimal totalPlanDeliveryQty;

    /**
     * IQC暂收数量
     */
    @ApiModelProperty(name="iqcTemporaryQty",value = "IQC暂收数量")
    @Excel(name = "IQC暂收数量", height = 20, width = 30,orderNum="")
    @Column(name = "iqc_temporary_qty")
    private BigDecimal iqcTemporaryQty;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    @Excel(name = "累计下发数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    @Excel(name = "是否已全部下发(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

    /**
     * 送货地址
     */
    @ApiModelProperty(name="deliveryAddress",value = "送货地址")
    @Excel(name = "送货地址", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_address")
    private String deliveryAddress;

    /**
     * 物料状态(1、装车2、发货3、已收货)
     */
    @ApiModelProperty(name="materialStatus",value = "物料状态(1、装车2、发货3、已收货)")
    @Excel(name = "物料状态(1、装车2、发货3、已收货)", height = 20, width = 30,orderNum="")
    @Column(name = "material_status")
    private Byte materialStatus;

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

    @Transient
    @ApiModelProperty("下发数量")
    private BigDecimal qty;

    private static final long serialVersionUID = 1L;
}
