package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 生产领料表（发料计划）
 * wms_out_production_material
 * @author hyc
 * @date 2021-01-18 19:49:50
 */
@Data
@Table(name = "wms_out_production_material")
public class WmsOutProductionMaterial extends ValidGroup implements Serializable {
    /**
     * 主键ID
     */
    @ApiModelProperty(name="productionMaterialId",value = "主键ID")
    @NotNull(groups = update.class,message = "主键ID不能为空")
    @Id
    @Column(name = "production_material_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long productionMaterialId;

    /**
     * 生产领料单号
     */
    @ApiModelProperty(name="productionMaterialCode",value = "生产领料单号")
    @Excel(name = "生产领料单号", height = 20, width = 30,orderNum="1")
    @Column(name = "production_material_code")
    private String productionMaterialCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Column(name = "pro_line_id")
    private Long proLineId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 计划发料数量
     */
    @ApiModelProperty(name="planQty",value = "计划发料数量")
    @Excel(name = "计划发料数量", height = 20, width = 30,orderNum="6")
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 实发数量
     */
    @ApiModelProperty(name="realityQty",value = "实发数量")
    @Excel(name = "实发数量", height = 20, width = 30,orderNum="7")
    @Column(name = "reality_qty")
    private BigDecimal realityQty;

    /**
     * 发料日期
     */
    @ApiModelProperty(name="outTime",value = "发料日期")
    @Excel(name = "发料日期", height = 20, width = 30,orderNum="8")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 储位ID
     */
    @ApiModelProperty(name="storageId",value = "储位ID")
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * 单据状态（0-待发料 1-部分发料 2-发料完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待发料 1-部分发料 2-发料完成）")
    @Excel(name = "单据状态（0-待发料 1-部分发料 2-发料完成）", height = 20, width = 30,orderNum="13",replace = {"待发料_0","部分发料_1","发料完成_2"})
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="14")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    @Transient
    @ApiModelProperty(name="finishedProductCode",value = "完工入库单号")
    private String finishedProductCode;

    private static final long serialVersionUID = 1L;
}