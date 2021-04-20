package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OmSalesOrderDetDto {
    /**
     * 客户订单行号
     */
    @ApiModelProperty(name="customerOrderLineNumber",value = "客户订单行号")
    @Excel(name = "客户订单行号", height = 20, width = 30,orderNum="")
    @Column(name = "customer_order_line_number")
    private Long customerOrderLineNumber;

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
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    @Excel(name = "状态", height = 20, width = 30,orderNum="")
    private Byte status;

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
    @Column(name = "plan_revert_date")
    private Date planRevertDate;

    /**
     * 计划发货日期
     */
    @ApiModelProperty(name="planDeliverDate",value = "计划发货日期")
    @Excel(name = "计划发货日期", height = 20, width = 30,orderNum="")
    @Column(name = "plan_deliver_date")
    private Date planDeliverDate;

    /**
     * 计划交货日期
     */
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Excel(name = "计划交货日期", height = 20, width = 30,orderNum="")
    @Column(name = "plan_delivery_date")
    private Date planDeliveryDate;

    /**
     * 实际发货日期
     */
    @ApiModelProperty(name="actualDeliverDate",value = "实际发货日期")
    @Excel(name = "实际发货日期", height = 20, width = 30,orderNum="")
    @Column(name = "actual_deliver_date")
    private Date actualDeliverDate;

    /**
     * 实际交货日期
     */
    @ApiModelProperty(name="actualDeliveryDate",value = "实际交货日期")
    @Excel(name = "实际交货日期", height = 20, width = 30,orderNum="")
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
     * 备注说明
     */

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

}
