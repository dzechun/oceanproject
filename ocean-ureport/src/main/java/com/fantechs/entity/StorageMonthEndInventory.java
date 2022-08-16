package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 月末库存表
 *
 * @date 2021-03-02 11:31:51
 */
@Data
@Table(name = "storage_month_end_inventory")
public class StorageMonthEndInventory extends ValidGroup implements Serializable {
    /**
     * 月末库存ID
     */
    @ApiModelProperty(name = "monthEndInventoryId", value = "月末库存ID")
    @Id
    @Column(name = "month_end_inventory_id")
    private Long monthEndInventoryId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name = "storageId", value = "储位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId", value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 物料条码编码
     */
    @ApiModelProperty(name = "materialBarcodeCode", value = "物料条码编码")
    @Column(name = "material_barcode_code")
    private String materialBarcodeCode;

    /**
     * 箱数
     */
    @ApiModelProperty(name = "boxNumber", value = "箱数")
    @Excel(name = "箱数", height = 20, width = 30, orderNum = "9")
    @Column(name = "box_number")
    private Integer boxNumber;

    /**
     * 总数
     */
    @ApiModelProperty(name = "total", value = "总数")
    @Excel(name = "总数", height = 20, width = 30, orderNum = "10")
    private Integer total;

    /**
     * 物料总数
     */
    @ApiModelProperty(name = "materialTotal", value = "物料总数")
    private Integer materialTotal;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name = "status", value = "状态（0、无效 1、有效）")
    private Byte status;

    /**
     * 组织id
     */
    @ApiModelProperty(name = "organizationId", value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name = "createUserId", value = "创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name = "modifiedUserId", value = "修改人ID")
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name = "modifiedTime", value = "修改时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name = "isDelete", value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

//    /**
//     * 库存明细
//     */
//    @ApiModelProperty(name = "list", value = "库存明细")
//    private List<WmsInnerStorageInventoryDetDto> list;

    private static final long serialVersionUID = 1L;
}
