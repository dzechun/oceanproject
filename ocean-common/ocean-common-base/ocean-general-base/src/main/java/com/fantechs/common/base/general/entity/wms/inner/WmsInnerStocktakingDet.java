package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 盘点单明细信息表
 * wms_inner_stocktaking_det
 * @author 53203
 * @date 2021-03-22 16:37:12
 */
@Data
@Table(name = "wms_inner_stocktaking_det")
public class WmsInnerStocktakingDet extends ValidGroup implements Serializable {
    /**
     * 盘点单明细id
     */
    @ApiModelProperty(name="stocktakingDetId",value = "盘点单明细id")
    @Excel(name = "盘点单明细id", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "stocktaking_det_id")
    private Long stocktakingDetId;

    /**
     * 盘点单id
     */
    @ApiModelProperty(name="stocktakingId",value = "盘点单id")
    @Excel(name = "盘点单id", height = 20, width = 30,orderNum="") 
    @Column(name = "stocktaking_id")
    private Long stocktakingId;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="") 
    @Column(name = "contract_code")
    private String contractCode;

    /**
     * 栈板码
     */
    @ApiModelProperty(name="palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30,orderNum="") 
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Excel(name = "物料id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @Excel(name = "储位id", height = 20, width = 30,orderNum="")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 账面数量
     */
    @ApiModelProperty(name="bookInventory",value = "账面数量")
    @Excel(name = "账面数量", height = 20, width = 30,orderNum="") 
    @Column(name = "book_inventory")
    private BigDecimal bookInventory;

    /**
     * 实盘数量
     */
    @ApiModelProperty(name="countedQuantity",value = "实盘数量")
    @Excel(name = "实盘数量", height = 20, width = 30,orderNum="") 
    @Column(name = "counted_quantity")
    private BigDecimal countedQuantity;

    /**
     * 盈亏数量
     */
    @ApiModelProperty(name="profitLossQuantity",value = "盈亏数量")
    @Excel(name = "盈亏数量", height = 20, width = 30,orderNum="") 
    @Column(name = "profit_loss_quantity")
    private BigDecimal profitLossQuantity;

    /**
     * 盈亏率
     */
    @ApiModelProperty(name="profitLossRate",value = "盈亏率")
    @Excel(name = "盈亏率", height = 20, width = 30,orderNum="") 
    @Column(name = "profit_loss_rate")
    private BigDecimal profitLossRate;

    /**
     * 出库率
     */
    @ApiModelProperty(name="deliveryRate",value = "出库率")
    @Excel(name = "出库率", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_rate")
    private BigDecimal deliveryRate;

    /**
     * 单价
     */
    @ApiModelProperty(name="price",value = "单价")
    @Excel(name = "单价", height = 20, width = 30,orderNum="") 
    private BigDecimal price;

    /**
     * 盈亏金额
     */
    @ApiModelProperty(name="profitLossAmount",value = "盈亏金额")
    @Excel(name = "盈亏金额", height = 20, width = 30,orderNum="") 
    @Column(name = "profit_loss_amount")
    private BigDecimal profitLossAmount;

    /**
     * 盘点状态(0、待盘点 1、盘点完成)
     */
    @ApiModelProperty(name="status",value = "盘点状态(0、待盘点 1、盘点完成)")
    @Excel(name = "盘点状态(0、待盘点 1、盘点完成)", height = 20, width = 30,orderNum="") 
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
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

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

    private static final long serialVersionUID = 1L;
}