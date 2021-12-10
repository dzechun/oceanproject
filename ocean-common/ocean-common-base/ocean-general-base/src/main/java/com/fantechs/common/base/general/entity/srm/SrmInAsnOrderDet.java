package com.fantechs.common.base.general.entity.srm;

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
 * ASN单明细
 * wms_in_asn_order_det
 * @author 81947
 * @date 2021-11-25 09:19:10
 */
@Data
@Table(name = "wms_in_asn_order_det")
public class SrmInAsnOrderDet extends ValidGroup implements Serializable {
    /**
     * ASN单明细ID
     */
    @ApiModelProperty(name="asnOrderDetId",value = "ASN单明细ID")
    @Excel(name = "ASN单明细ID", height = 20, width = 30)
    @Id
    @Column(name = "asn_order_det_id")
    private Long asnOrderDetId;

    /**
     * ASN单ID
     */
    @ApiModelProperty(name="asnOrderId",value = "ASN单ID")
    @Excel(name = "ASN单ID", height = 20, width = 30)
    @Column(name = "asn_order_id")
    private Long asnOrderId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 订单ID
     */
    @ApiModelProperty(name="sourceOrderId",value = "订单ID")
    @Excel(name = "订单ID", height = 20, width = 30)
    @Column(name = "source_order_id")
    private Long sourceOrderId;

    /**
     * 核心单据明细ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心单据明细ID")
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 订单明细ID
     */
    @ApiModelProperty(name="orderDetId",value = "订单明细ID")
    @Excel(name = "订单明细ID", height = 20, width = 30)
    @Column(name = "order_det_id")
    private Long orderDetId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Excel(name = "库位ID", height = 20, width = 30)
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Excel(name = "库存状态ID", height = 20, width = 30)
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 行号
     */
    @ApiModelProperty(name="lineNumber",value = "行号")
    @Excel(name = "行号", height = 20, width = 30)
    @Column(name = "line_number")
    private String lineNumber;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 交货数量
     */
    @ApiModelProperty(name="deliveryQty",value = "交货数量")
    @Excel(name = "交货数量", height = 20, width = 30)
    @Column(name = "delivery_qty")
    private BigDecimal deliveryQty;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    @Excel(name = "托盘号", height = 20, width = 30)
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 收货日期/交货日期
     */
    @ApiModelProperty(name="receivingDate",value = "收货日期/交货日期")
    @Excel(name = "交货日期", height = 20, width = 30)
    @Column(name = "receiving_date")
    private Date receivingDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30)
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 累计下发数量
     */
    @ApiModelProperty(name="totalIssueQty",value = "累计下发数量")
    @Column(name = "total_issue_qty")
    private BigDecimal totalIssueQty;

    /**
     * 是否已全部下发(0-否 1-是)
     */
    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    @Column(name = "if_all_issued")
    private Byte ifAllIssued;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
