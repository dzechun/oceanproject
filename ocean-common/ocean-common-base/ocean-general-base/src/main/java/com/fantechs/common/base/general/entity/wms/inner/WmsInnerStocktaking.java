package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 盘点单信息表
 * wms_inner_stocktaking
 * @author 53203
 * @date 2020-12-16 19:51:30
 */
@Data
@Table(name = "wms_inner_stocktaking")
public class WmsInnerStocktaking extends ValidGroup implements Serializable {
    /**
     * 盘点单id
     */
    @ApiModelProperty(name="stocktakingId",value = "盘点单id")
    @Id
    @Column(name = "stocktaking_id")
    @NotNull(groups = update.class,message = "盘点单id不能为空")
    private Long stocktakingId;

    /**
     * 盘点单号
     */
    @ApiModelProperty(name="stocktakingCode",value = "盘点单号")
    @Excel(name = "盘点单号", height = 20, width = 30,orderNum="1")
    @Column(name = "stocktaking_code")
    @NotBlank(message = "盘点单号不能为空")
    private String stocktakingCode;

    /**
     * 线别id
     */
    @ApiModelProperty(name="proLineId",value = "线别id")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Column(name = "material_id")
    @NotNull(message = "物料id不能为空")
    private Long materialId;

    /**
     * 账面数量
     */
    @ApiModelProperty(name="bookInventory",value = "账面数量")
    @Excel(name = "账面数量", height = 20, width = 30,orderNum="11")
    @Column(name = "book_inventory")
    private BigDecimal bookInventory;

    /**
     * 已盘点数量
     */
    @ApiModelProperty(name="countedQuantity",value = "已盘点数量")
    @Excel(name = "已盘点数量", height = 20, width = 30,orderNum="12")
    @Column(name = "counted_quantity")
    private BigDecimal countedQuantity;

    /**
     * 盈亏数量
     */
    @ApiModelProperty(name="profitLossQuantity",value = "盈亏数量")
    @Excel(name = "盈亏数量", height = 20, width = 30,orderNum="13")
    @Column(name = "profit_loss_quantity")
    private BigDecimal profitLossQuantity;

    /**
     * 出库率
     */
    @ApiModelProperty(name="deliveryRate",value = "出库率")
    @Excel(name = "出库率", height = 20, width = 30,orderNum="14")
    @Column(name = "delivery_rate")
    private String deliveryRate;

    /**
     * 单价
     */
    @ApiModelProperty(name="price",value = "单价")
    @Excel(name = "单价", height = 20, width = 30,orderNum="15")
    private BigDecimal price;

    /**
     * 盈亏金额
     */
    @ApiModelProperty(name="profitLossAmount",value = "盈亏金额")
    @Excel(name = "盈亏金额", height = 20, width = 30,orderNum="16")
    @Column(name = "profit_loss_amount")
    private BigDecimal profitLossAmount;

    /**
     * 盘点员Id
     */
    @ApiModelProperty(name="stockistId",value = "盘点员Id")
    @Column(name = "stockist_id")
    private Long stockistId;

    /**
     * 盘点状态(0、待盘点 1、盘点中 2、盘点完成)
     */
    @ApiModelProperty(name="status",value = "盘点状态(0、待盘点 1、盘点中 2、盘点完成)")
    @Excel(name = "盘点状态(0、待盘点 1、盘点中 2、盘点完成)", height = 20, width = 30,orderNum="19")
    private Byte status;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="21",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="22",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}