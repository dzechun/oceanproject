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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 采退订单明细表
 * om_purchase_return_order_det
 * @author admin
 * @date 2021-12-20 17:04:52
 */
@Data
@Table(name = "om_purchase_return_order_det")
public class OmPurchaseReturnOrderDet extends ValidGroup implements Serializable {
    /**
     * 采退订单明细表ID
     */
    @ApiModelProperty(name="purchaseReturnOrderDetId",value = "采退订单明细表ID")
    @Id
    @Column(name = "purchase_return_order_det_id")
    private Long purchaseReturnOrderDetId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    //@Excel(name = "核心单据编码", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    //@Excel(name = "来源单据编码", height = 20, width = 30,orderNum="")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 核心来源ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心来源ID")
    //@Excel(name = "核心来源ID", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    //@Excel(name = "来源ID", height = 20, width = 30,orderNum="")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 采购订单明细ID
     */
    @ApiModelProperty(name="purchaseOrderDetId",value = "采购订单明细ID")
    //@Excel(name = "采购订单明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_order_det_id")
    private Long purchaseOrderDetId;

    /**
     * 采退订单ID
     */
    @ApiModelProperty(name="purchaseReturnOrderId",value = "采退订单ID")
    //@Excel(name = "采退订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_return_order_id")
    private Long purchaseReturnOrderId;

    /**
     * 采购单号
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购单号")
    //@Excel(name = "采购单号", height = 20, width = 30,orderNum="")
    @Column(name = "purchase_order_code")
    private String purchaseOrderCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    //@Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    //@Excel(name = "仓库ID", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 单位名称
     */
    @ApiModelProperty(name="unitName",value = "单位名称")
    @Excel(name = "单位名称", height = 20, width = 30,orderNum="10")
    @Column(name = "unit_name")
    private String unitName;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQty",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="11")
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 实际拣货数量
     */
    @ApiModelProperty(name="actualQty",value = "实际拣货数量")
    @Excel(name = "实际拣货数量", height = 20, width = 30,orderNum="12")
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    //@Excel(name = "批次号", height = 20, width = 30,orderNum="")
    @Column(name = "batch_code")
    private String batchCode;

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
    //@Excel(name = "状态", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    //@Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String remark;

    /**
     * 组织ID
     */
    @ApiModelProperty(name="orgId",value = "组织ID")
    //@Excel(name = "组织ID", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    //@Excel(name = "创建人ID", height = 20, width = 30,orderNum="")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    //@Excel(name = "创建时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    //@Excel(name = "修改人ID", height = 20, width = 30,orderNum="")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    //@Excel(name = "修改时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private static final long serialVersionUID = 1L;
}