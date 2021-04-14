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
import java.util.List;

;

/**
 * 储位库存表
 * smt_storage_inventory
 * @date 2020-12-04 13:41:50
 */
@Data
@Table(name = "wms_inner_storage_inventory")
public class WmsInnerStorageInventory extends ValidGroup implements Serializable {
    /**
     * 储位库存ID
     */
    @ApiModelProperty(name="storageInventoryId",value = "储位库存ID")
    @Excel(name = "储位库存ID", height = 20, width = 30)
    @Id
    @Column(name = "storage_inventory_id")
    private Long storageInventoryId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Excel(name = "储位ID", height = 20, width = 30)
    @Column(name = "storage_id")
    @NotNull(groups = update.class,message = "储位id不能为空")
    private Long storageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    @NotNull(groups = update.class,message = "物料id不能为空")
    private Long materialId;

    /**
     * 等级
     */
    @ApiModelProperty(name="level",value = "等级")
    @Excel(name = "等级", height = 20, width = 30)
    @Column(name = "level")
    private String level;

    /**
     * 数量
     */
    @ApiModelProperty(name="quantity",value = "数量")
    @Excel(name = "数量", height = 20, width = 30)
    @Column(name = "quantity")
    private BigDecimal quantity;

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

    private List<WmsInnerStorageInventoryDet> smtStorageInventoryDets;

    private static final long serialVersionUID = 1L;
}
