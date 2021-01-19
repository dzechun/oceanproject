package com.fantechs.common.base.general.entity.wms.out.history;

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
 * 生产领料履历表（发料计划）
 * wms_out_ht_production_material
 * @author hyc
 * @date 2021-01-18 19:49:52
 */
@Data
@Table(name = "wms_out_ht_production_material")
public class WmsOutHtProductionMaterial extends ValidGroup implements Serializable {
    /**
     * 履历主键ID
     */
    @ApiModelProperty(name="htProductionMaterialId",value = "履历主键ID")
    @NotNull(groups = update.class,message = "履历ID不能为空")
    @Id
    @Column(name = "ht_production_material_id")
    private Long htProductionMaterialId;

    /**
     * 生产领料表ID
     */
    @ApiModelProperty(name="productionMaterialId",value = "生产领料表ID")
    @Excel(name = "生产领料表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "production_material_id")
    private Long productionMaterialId;

    /**
     * 生产领料单号
     */
    @ApiModelProperty(name="productionMaterialCode",value = "生产领料单号")
    @Excel(name = "生产领料单号", height = 20, width = 30,orderNum="") 
    @Column(name = "production_material_code")
    private String productionMaterialCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Excel(name = "产线ID", height = 20, width = 30,orderNum="") 
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 计划发料数量
     */
    @ApiModelProperty(name="planQty",value = "计划发料数量")
    @Excel(name = "计划发料数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 实发数量
     */
    @ApiModelProperty(name="realityQty",value = "实发数量")
    @Excel(name = "实发数量", height = 20, width = 30,orderNum="")
    @Column(name = "reality_qty")
    private BigDecimal realityQty;

    /**
     * 发料日期
     */
    @ApiModelProperty(name="outTime",value = "发料日期")
    @Excel(name = "发料日期", height = 20, width = 30,orderNum="") 
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Excel(name = "储位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 单据状态（0-待发料 1-部分发料 2-发料完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待发料 1-部分发料 2-发料完成）")
    @Excel(name = "单据状态（0-待发料 1-部分发料 2-发料完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="") 
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
    private Byte status;

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

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="")
    private String workOrderCode;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proLineName" ,value="产线名称")
    @Excel(name = "产线名称", height = 20, width = 30,orderNum="")
    private String proLineName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName" ,value="物料名称")
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="")
    private String materialName;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode" ,value="仓库编码")
    @Excel(name = "仓库编码", height = 20, width = 30,orderNum="")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="")
    private String warehouseName;

    /**
     * 储位编码
     */
    @ApiModelProperty(name="storageCode" ,value="储位编码")
    @Excel(name = "储位编码", height = 20, width = 30,orderNum="")
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name="storageName" ,value="储位名称")
    @Excel(name = "储位名称", height = 20, width = 30,orderNum="")
    private String storageName;

    private static final long serialVersionUID = 1L;
}