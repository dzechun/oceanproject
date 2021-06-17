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
 * 库存盘点明细
 * wms_inventory_verification_det
 * @author mr.lei
 * @date 2021-05-27 18:21:54
 */
@Data
@Table(name = "wms_inner_stock_order_det")
public class WmsInnerStockOrderDet extends ValidGroup implements Serializable {
    @Id
    @Column(name = "stock_order_det_id")
    private Long stockOrderDetId;

    @Column(name = "stock_order_id")
    private Long stockOrderId;

    /**
     * 相关明细ID
     */
    @ApiModelProperty(name="sourceDetId",value = "相关明细ID")
    @Excel(name = "相关明细ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_det_id")
    private Long sourceDetId;

    /**
     * 储位id
     */
    @ApiModelProperty(name="storageId",value = "储位id")
    @Excel(name = "储位id", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    @Excel(name = "物料id", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 原始数量
     */
    @ApiModelProperty(name="originalQty",value = "原始数量")
    @Excel(name = "原始数量", height = 20, width = 30,orderNum="") 
    @Column(name = "original_qty")
    private BigDecimal originalQty;

    /**
     * 盘点数量
     */
    @ApiModelProperty(name="stockQty",value = "盘点数量")
    @Excel(name = "盘点数量", height = 20, width = 30,orderNum="") 
    @Column(name = "stock_qty")
    private BigDecimal stockQty;

    /**
     * 差异数量
     */
    @ApiModelProperty(name="varianceQty",value = "差异数量")
    @Excel(name = "差异数量", height = 20, width = 30,orderNum="") 
    @Column(name = "variance_qty")
    private BigDecimal varianceQty;

    /**
     * 上次差异数量
     */
    @ApiModelProperty(name="lastTimeVarianceQty",value = "上次差异数量")
    @Excel(name = "差异数量", height = 20, width = 30,orderNum="")
    @Column(name = "last_time_variance_qty")
    private BigDecimal lastTimeVarianceQty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

    @ApiModelProperty(name = "stockUserId",value = "盘点人员id")
    @Excel(name = "盘点人员id",height = 20,width = 30,orderNum = "")
    @Column(name = "stock_user_id")
    private Long stockUserId;

    /**
     * 是否登记 1-是 2-否
     */
    @ApiModelProperty(name="ifRegister",value = "是否登记 1-是 2-否")
    @Excel(name = "是否登记 1-是 2-否", height = 20, width = 30,orderNum="") 
    private Byte ifRegister;

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
    @Column(name = "org_id")
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

    @ApiModelProperty(name = "barcode",value = "PDA盘点确认条码记录")
    @Column(name = "barcode")
    private String barcode;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    @Excel(name = "托盘号", height = 20, width = 30,orderNum="18")
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    private static final long serialVersionUID = 1L;
}