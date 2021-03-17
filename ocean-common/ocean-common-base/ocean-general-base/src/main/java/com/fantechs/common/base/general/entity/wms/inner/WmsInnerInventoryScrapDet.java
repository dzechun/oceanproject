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
;

/**
 * 调拨单明细信息表
 * wms_inner_inventory_scrap_det
 * @author hyc
 * @date 2021-03-10 16:14:47
 */
@Data
@Table(name = "wms_inner_inventory_scrap_det")
public class WmsInnerInventoryScrapDet extends ValidGroup implements Serializable {
    /**
     * 盘存转报废单明细ID
     */
    @ApiModelProperty(name="inventoryScrapDetId",value = "盘存转报废单明细ID")
    @Id
    @NotNull(groups = update.class,message = "盘存转报废单明细ID不能为空")
    @Column(name = "inventory_scrap_det_id")
    private Long inventoryScrapDetId;

    /**
     * 盘存转报废单ID
     */
    @ApiModelProperty(name="inventoryScrapId",value = "盘存转报废单ID")
    @Excel(name = "盘存转报废单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_scrap_id")
    private Long inventoryScrapId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Excel(name = "储位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 栈板条码
     */
    @ApiModelProperty(name="barCode",value = "栈板条码")
    @Excel(name = "栈板条码", height = 20, width = 30,orderNum="")
    @Column(name = "bar_code")
    private String barCode;

    /**
     * 计划报废箱数
     */
    @ApiModelProperty(name="planCartonQty",value = "计划报废箱数")
    @Excel(name = "计划报废箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_carton_qty")
    private BigDecimal planCartonQty;

    /**
     * 计划报废总数
     */
    @ApiModelProperty(name="planTotalQty",value = "计划报废总数")
    @Excel(name = "计划报废总数", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_total_qty")
    private BigDecimal planTotalQty;

    /**
     * 实际报废箱数
     */
    @ApiModelProperty(name="realityCartonQty",value = "实际报废箱数")
    @Excel(name = "实际报废箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_carton_qty")
    private BigDecimal realityCartonQty;

    /**
     * 实际报废总数
     */
    @ApiModelProperty(name="realityTotalQty",value = "实际报废总数")
    @Excel(name = "实际报废总数", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_total_qty")
    private BigDecimal realityTotalQty;

    /**
     * 单据状态（0-待报废 1-报废中 2-报废完成）
     */
    @ApiModelProperty(name="inventoryScrapStatus",value = "单据状态（0-待报废 1-报废中 2-报废完成）")
    @Excel(name = "单据状态（0-待报废 1-报废中 2-报废完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "inventory_scrap_status")
    private Byte inventoryScrapStatus;

    /**
     * 栈板状态（0-未扫描，1-已扫描）
     */
    @ApiModelProperty(name="barCodeStatus",value = "栈板状态（0-未扫描，1-已扫描）")
    @Excel(name = "栈板状态（0-未扫描，1-已扫描）", height = 20, width = 30,orderNum="")
    @Column(name = "bar_code_status")
    private Byte barCodeStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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