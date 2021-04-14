package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 储位库存明细表
 * smt_storage_inventory_det
 * @date 2020-12-04 14:39:37
 */
@Data
@Table(name = "wms_inner_storage_inventory_det")
public class WmsInnerStorageInventoryDet extends ValidGroup implements Serializable {
    /**
     * 储位库存明细ID
     */
    @ApiModelProperty(name="storageInventoryDetId",value = "储位库存明细ID")
    @Excel(name = "储位库存明细ID", height = 20, width = 30)
    @Id
    @Column(name = "storage_inventory_det_id")
    private Long storageInventoryDetId;

    /**
     * 储位库存ID
     */
    @ApiModelProperty(name="storageInventoryId",value = "储位库存ID")
    @Excel(name = "储位库存ID", height = 20, width = 30)
    @Column(name = "storage_inventory_id")
    private Long storageInventoryId;

    /**
     * 物料条码编码
     */
    @ApiModelProperty(name="materialBarcodeCode",value = "物料条码编码")
    @Excel(name = "物料条码编码", height = 20, width = 30)
    @Column(name = "material_barcode_code")
    private String materialBarcodeCode;

    /**
     * 入库单号
     */
    @ApiModelProperty(name="godownEntry",value = "入库单号")
    @Excel(name = "入库单号", height = 20, width = 30)
    @Column(name = "godown_entry")
    private String godownEntry;

    /**
     * 物料数量
     */
    @ApiModelProperty(name="materialQuantity",value = "物料数量")
    @Excel(name = "物料数量", height = 20, width = 30)
    @Column(name = "material_quantity")
    @NotNull(groups = update.class,message = "物料数量不能为空")
    private BigDecimal materialQuantity;

    /**
     * 生产批号
     */
    @ApiModelProperty(name="productionCode",value = "生产批号")
    @Excel(name = "生产批号", height = 20, width = 30)
    @Column(name = "production_code")
    private String productionCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30)
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30)
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30)
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

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
