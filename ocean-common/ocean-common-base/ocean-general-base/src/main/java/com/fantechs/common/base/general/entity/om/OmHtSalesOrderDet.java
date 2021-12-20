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
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 销售订单明细履历表
 * om_ht_sales_order_det
 * @author Law
 * @date 2021-04-21 14:55:38
 */
@Data
@Table(name = "om_ht_sales_order_det")
public class OmHtSalesOrderDet extends ValidGroup implements Serializable {
    /**
     * 销售订单履历ID
     */
    @ApiModelProperty(name="htSalesOrderDetId",value = "销售订单履历ID")
//    @Excel(name = "销售订单履历ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_sales_order_det_id")
    private Long htSalesOrderDetId;

    /**
     * 销售订单明细ID
     */
    @ApiModelProperty(name="salesOrderDetId",value = "销售订单明细ID")
//    @Excel(name = "销售订单明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "sales_order_det_id")
    private Long salesOrderDetId;

    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
    @Excel(name = "销售订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "sales_order_id")
    private Long salesOrderId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name = "coreSourceOrderCode",value = "核心单据编码")
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name = "sourceOrderCode",value = "来源单据编码")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 核心来源ID
     */
    @ApiModelProperty(name = "coreSourceId",value = "核心来源ID")
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name = "sourceId",value = "来源ID")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 行号
     */
    @ApiModelProperty(name = "lineNumber",value = "行号")
    @Column(name = "line_number")
    private String lineNumber;

    /**
     * 客户订单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户订单号")
    @Excel(name = "客户订单号", height = 20, width = 30,orderNum="")
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    /**
     * 来源行号
     */
    @ApiModelProperty(name="sourceLineNumber",value = "来源行号")
    @Excel(name = "来源行号", height = 20, width = 30,orderNum="")
    @Column(name = "source_line_number")
    private String sourceLineNumber;

    /**
     * 项目号
     */
    @ApiModelProperty(name="projectCode",value = "项目号")
    @Excel(name = "项目号", height = 20, width = 30,orderNum="")
    @Column(name = "project_code")
    private String projectCode;

    /**
     * 内部批号
     */
    @ApiModelProperty(name="innerBatchNumber",value = "内部批号")
    @Excel(name = "内部批号", height = 20, width = 30,orderNum="")
    @Column(name = "inner_batch_number")
    private String innerBatchNumber;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="")
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 类型
     */
    @ApiModelProperty(name="type",value = "类型")
    @Excel(name = "类型", height = 20, width = 30,orderNum="")
    private Byte type;

    /**
     * 是否赠品(0-否 1-是)
     */
    @ApiModelProperty(name="ifFreeGift",value = "是否赠品(0-否 1-是)")
    @Excel(name = "是否赠品(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "if_free_gift")
    private Byte ifFreeGift;

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
     * 出货数量
     */
    @ApiModelProperty(name="actualQty",value = "出货数量")
    @Excel(name = "出货数量", height = 20, width = 30,orderNum="")
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 计划回复日期
     */
    @ApiModelProperty(name="planRevertDate",value = "计划回复日期")
    @Excel(name = "计划回复日期", height = 20, width = 30,orderNum="")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name = "plan_revert_date")
    private Date planRevertDate;

    /**
     * 计划发货日期
     */
    @ApiModelProperty(name="planDeliverDate",value = "计划发货日期")
    @Excel(name = "计划发货日期", height = 20, width = 30,orderNum="")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name = "plan_deliver_date")
    private Date planDeliverDate;

    /**
     * 实际发货日期
     */
    @ApiModelProperty(name="actualDeliverDate",value = "实际发货日期")
    @Excel(name = "实际发货日期", height = 20, width = 30,orderNum="")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name = "actual_deliver_date")
    private Date actualDeliverDate;

    /**
     * 计划交货日期
     */
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Excel(name = "计划交货日期", height = 20, width = 30,orderNum="")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name = "plan_delivery_date")
    private Date planDeliveryDate;

    /**
     * 实际交货日期
     */
    @ApiModelProperty(name="actualDeliveryDate",value = "实际交货日期")
    @Excel(name = "实际交货日期", height = 20, width = 30,orderNum="")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name = "actual_delivery_date")
    private Date actualDeliveryDate;

    /**
     * 送货地址
     */
    @ApiModelProperty(name="deliveryAddress",value = "送货地址")
    @Excel(name = "送货地址", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_address")
    private String deliveryAddress;

    /**
     * 累计通知发货数量
     */
    @ApiModelProperty(name="totalInformDeliverQty",value = "累计通知发货数量")
    @Excel(name = "累计通知发货数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_inform_deliver_qty")
    private BigDecimal totalInformDeliverQty;

    /**
     * 安排发运数量
     */
    @ApiModelProperty(name="arrangeDispatchQty",value = "安排发运数量")
    @Excel(name = "安排发运数量", height = 20, width = 30,orderNum="")
    @Column(name = "arrange_dispatch_qty")
    private BigDecimal arrangeDispatchQty;

    /**
     * 累计出库数量
     */
    @ApiModelProperty(name="totalOutboundQty",value = "累计出库数量")
    @Excel(name = "累计出库数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_outbound_qty")
    private BigDecimal totalOutboundQty;

    /**
     * 累计申请数量
     */
    @ApiModelProperty(name="totalSalesReturnApplyQty",value = "累计申请数量")
    @Excel(name = "累计申请数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_sales_return_apply_qty")
    private BigDecimal totalSalesReturnApplyQty;

    /**
     * 累计退库数量
     */
    @ApiModelProperty(name="totalReturnQty",value = "累计退库数量")
    @Excel(name = "累计退库数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_return_qty")
    private BigDecimal totalReturnQty;

    /**
     * 订货数量
     */
    @ApiModelProperty(name="orderGoodsQty",value = "订货数量")
    @Excel(name = "订货数量", height = 20, width = 30,orderNum="")
    @Column(name = "order_goods_qty")
    private BigDecimal orderGoodsQty;

    /**
     * 未订货数量
     */
    @ApiModelProperty(name="notOrderGoodsQty",value = "未订货数量")
    @Excel(name = "未订货数量", height = 20, width = 30,orderNum="")
    @Column(name = "not_order_goods_qty")
    private BigDecimal notOrderGoodsQty;

    /**
     * 累计退货需补给数量
     */
    @ApiModelProperty(name="totalReturnNeedSupplyQty",value = "累计退货需补给数量")
    @Excel(name = "累计退货需补给数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_return_need_supply_qty")
    private BigDecimal totalReturnNeedSupplyQty;

    /**
     * 累计生产数量
     */
    @ApiModelProperty(name="totalProductionQty",value = "累计生产数量")
    @Excel(name = "累计生产数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_production_qty")
    private BigDecimal totalProductionQty;

    /**
     * 累计未生产数量
     */
    @ApiModelProperty(name="totalNotProductionQty",value = "累计未生产数量")
    @Excel(name = "累计未生产数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_not_production_qty")
    private BigDecimal totalNotProductionQty;

    /**
     * 退货安排发运数量
     */
    @ApiModelProperty(name="returnArrangeDispatchQty",value = "退货安排发运数量")
    @Excel(name = "退货安排发运数量", height = 20, width = 30,orderNum="")
    @Column(name = "return_arrange_dispatch_qty")
    private BigDecimal returnArrangeDispatchQty;

    /**
     * 行状态(1-作废”、2-审核、3-下达、4-冻结、5-关闭、6-变更中、7-完成)
     */
    @ApiModelProperty(name="lineStatus",value = "行状态(1-作废”、2-审核、3-下达、4-冻结、5-关闭、6-变更中、7-完成)")
    @Excel(name = "行状态(1-作废”、2-审核、3-下达、4-冻结、5-关闭、6-变更中、7-完成)", height = 20, width = 30,orderNum="")
    @Column(name = "line_status")
    private Byte lineStatus;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    //@Excel(name = "累计下发数量", height = 20, width = 30,orderNum="")
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    //@Excel(name = "是否已全部下发(0-否 1-是)", height = 20, width = 30,orderNum="")
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String remark;

    /**
     * 组织ID
     */
    @ApiModelProperty(name="orgId",value = "组织ID")
    @Excel(name = "组织ID", height = 20, width = 30,orderNum="")
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
     *销售订单号
     */
    @ApiModelProperty(name = "salesOrderCode", value = "销售订单号")
    @Excel(name = "销售订单号", height = 20, width = 30)
    private String salesOrderCode;

    @ApiModelProperty(name = "materialCode", value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;
    /**
     * 物料名称
     */
    @ApiModelProperty(name = "materialName", value = "物料名称")
    @Excel(name="物料名称" , height = 20, width = 30)
    private String materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name = "materialVersion", value = "物料版本")
    @Excel(name = "版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 物料描述
     */
    @ApiModelProperty(name = "materialDesc", value = "物料描述")
    @Excel(name = "物料描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 仓库名
     */
    @ApiModelProperty(name="warehouseName", value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30)
    private String warehouseName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;
    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    @Excel(name = "组织名称", height = 20, width = 30)
    private String organizationName;

    private static final long serialVersionUID = 1L;
}