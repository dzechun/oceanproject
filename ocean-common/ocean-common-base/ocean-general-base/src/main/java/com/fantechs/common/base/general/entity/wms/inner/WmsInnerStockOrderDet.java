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
    /**
     * 盘点单明细ID
     */
    @ApiModelProperty(name="stockOrderDetId",value = "盘点单明细ID")
    @Id
    @Column(name = "stock_order_det_id")
    private Long stockOrderDetId;

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
     * 核心来源ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心来源ID")
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源ID
     */
    @ApiModelProperty(name="sourceId",value = "来源ID")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 盘点单ID
     */
    @ApiModelProperty(name="stockOrderId",value = "盘点单ID")
    @Column(name = "stock_order_id")
    private Long stockOrderId;

    /**
     * 相关明细ID
     */
    @ApiModelProperty(name="sourceDetId",value = "相关明细ID")
    @Column(name = "source_det_id")
    private Long sourceDetId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 原始数量
     */
    @ApiModelProperty(name="originalQty",value = "原始数量")
    @Excel(name = "原始数量", height = 20, width = 30)
    @Column(name = "original_qty")
    private BigDecimal originalQty;

    /**
     * 盘点数量
     */
    @ApiModelProperty(name="stockQty",value = "盘点数量")
    @Excel(name = "盘点数量", height = 20, width = 30)
    @Column(name = "stock_qty")
    private BigDecimal stockQty;

    /**
     * 差异数量
     */
    @ApiModelProperty(name="varianceQty",value = "差异数量")
    @Excel(name = "差异数量", height = 20, width = 30)
    @Column(name = "variance_qty")
    private BigDecimal varianceQty;

    /**
     * 上次盘点差异数量
     */
    @ApiModelProperty(name="lastTimeVarianceQty",value = "上次盘点差异数量")
    @Excel(name = "上次盘点差异数量", height = 20, width = 30)
    @Column(name = "last_time_variance_qty")
    private BigDecimal lastTimeVarianceQty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30)
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionTime",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30)
    @Column(name = "production_time")
    private Date productionTime;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 工作人员ID
     */
    @ApiModelProperty(name="workerId",value = "工作人员ID")
    @Column(name = "worker_id")
    private Long workerId;

    /**
     * 盘点人ID
     */
    @ApiModelProperty(name="stockUserId",value = "盘点人ID")
    @Column(name = "stock_user_id")
    private Long stockUserId;

    /**
     * 是否已登记(0-否 1-是)
     */
    @ApiModelProperty(name="ifRegister",value = "是否已登记(0-否 1-是)")
    @Column(name = "if_register")
    private Byte ifRegister;

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
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}