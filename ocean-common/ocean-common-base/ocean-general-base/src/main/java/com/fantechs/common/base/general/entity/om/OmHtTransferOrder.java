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

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Data
@Table(name = "om_ht_transfer_order")
public class OmHtTransferOrder extends ValidGroup implements Serializable {

    @ApiModelProperty(name="htTransferOrderId",value = "调拨订单ID")
    @Id
    @Column(name = "ht_transfer_order_id")
    private Long htTransferOrderId;

    /**
     * 调拨订单ID
     */
    @ApiModelProperty(name="transferOrderId",value = "调拨订单ID")
    @Column(name = "transfer_order_id")
    private Long transferOrderId;

    /**
     * 调拨订单号
     */
    @ApiModelProperty(name="transferOrderCode",value = "调拨订单号")
    @Excel(name = "调拨订单号", height = 20, width = 30,orderNum="1")
    @Column(name = "transfer_order_code")
    private String transferOrderCode;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="2")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 调出仓库ID
     */
    @ApiModelProperty(name="outWarehouseId",value = "调出仓库ID")
    @Column(name = "out_warehouse_id")
    private Long outWarehouseId;

    /**
     * 调入仓库ID
     */
    @ApiModelProperty(name="inWarehouseId",value = "调入仓库ID")
    @Column(name = "in_warehouse_id")
    private Long inWarehouseId;

    /**
     * 订单总体积
     */
    @Transient
    @ApiModelProperty(name="totalVolume",value = "订单总体积")
    @Excel(name = "订单总体积", height = 20, width = 30,orderNum="7")
    private BigDecimal totalVolume;

    /**
     * 订单总净重
     */
    @Transient
    @ApiModelProperty(name="totalNetWeight",value = "订单总净重")
    @Excel(name = "订单总净重", height = 20, width = 30,orderNum="8")
    private BigDecimal totalNetWeight;

    /**
     * 订单总毛重
     */
    @Transient
    @ApiModelProperty(name="totalGrossWeight",value = "订单总毛重")
    @Excel(name = "订单总毛重", height = 20, width = 30,orderNum="9")
    private BigDecimal totalGrossWeight;

    /**
     * 订单状态(1-打开 2-已下发 3-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-打开 2-已下发 3-完成)")
    @Excel(name = "订单状态(1-打开 2-已下发 3-完成)", height = 20, width = 30,orderNum="10")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="11")
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="12")
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
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
}
