package com.fantechs.common.base.entity.storage;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * stm_storage_inventory
 * @Date 2020/12/2 16:01
 * @author 53203
 */
@Data
@Table(name = "smt_storage_inventory")
public class SmtStorageInventory extends ValidGroup implements Serializable {

    /**
     * 储位库存ID
     */
    @ApiModelProperty(name="storingInventoryId",value = "储位库存ID")
    @Id
    @Column(name = "storing_inventory_id")
    @NotNull(groups = update.class,message = "储位库存ID不能为空")
    private Long storingInventoryId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name = "storageId",value = "储位ID")
    @Column(name = "storage_id")
    @NotNull(message = "储位ID不能为空")
    private Long storageId;

    /**
     * 物料条码ID
     */
    @ApiModelProperty(name = "materialBarcodeId",value = "物料条码ID")
    @Column(name = "material_barcode_id")
    @NotNull(message = "物料条码ID不能为空")
    @Excel(name = "物料条码ID", height = 20, width = 30,orderNum="2")
    private Long materialBarcodeId;

    /**
     * 物料数量
     */
    @ApiModelProperty(name = "materialQuantity",value = "物料数量")
    @Column(name = "material_quantity")
    @NotNull(message = "物料数量不能为空")
    @Excel(name = "物料数量", height = 20, width = 30,orderNum="3")
    private Integer materialQuantity;

    /**
     * 生产日期
     */
    @ApiModelProperty(name = "productionDate",value = "生产日期")
    @Column(name = "production_date")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="4")
    private Date productionDate;

    /**
     * 生产批号
     */
    @ApiModelProperty(name = "productionBatchNumber",value = "生产批号")
    @Column(name = "production_batch_number")
    @Excel(name = "生产批号", height = 20, width = 30,orderNum="5")
    private String productionBatchNumber;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name = "supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 入库单号
     */
    @ApiModelProperty(name = "godownEntry",value = "入库单号")
    @Column(name = "godown_entry")
    @Excel(name = "入库单号", height = 20, width = 30,orderNum="7")
    private String godownEntry;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="17",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private static final long serialVersionUID = 1L;
}
