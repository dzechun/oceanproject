package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 生产领料表（发料计划）（部件明细）
 * wms_out_production_materiald_det
 * @author hyc
 * @date 2021-02-04 16:37:44
 */
@Data
@Table(name = "wms_out_production_materiald_det")
public class WmsOutProductionMaterialdDet extends ValidGroup implements Serializable {
    /**
     * 主键ID
     */
    @ApiModelProperty(name="productionMaterialDetId",value = "主键ID")
    @Excel(name = "主键ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "production_material_det_id")
    private Long productionMaterialDetId;

    /**
     * 生产领料表ID
     */
    @ApiModelProperty(name="productionMaterialId",value = "生产领料表ID")
    @Excel(name = "生产领料表ID", height = 20, width = 30,orderNum="") 
    @Column(name = "production_material_id")
    private Long productionMaterialId;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 实发数量
     */
    @ApiModelProperty(name="realityQty",value = "实发数量")
    @Excel(name = "实发数量", height = 20, width = 30,orderNum="") 
    @Column(name = "reality_qty")
    private BigDecimal realityQty;

    /**
     * 开工扫描数量
     */
    @ApiModelProperty(name="scanQty",value = "开工扫描数量")
    @Excel(name = "开工扫描数量", height = 20, width = 30,orderNum="") 
    @Column(name = "scan_qty")
    private BigDecimal scanQty;

    /**
     * 已配套数量
     */
    @ApiModelProperty(name="useQty",value = "已配套数量")
    @Excel(name = "已配套数量", height = 20, width = 30,orderNum="")
    @Column(name = "use_qty")
    private BigDecimal useQty;

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

    private static final long serialVersionUID = 1L;
}