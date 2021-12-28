package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.support.ValidGroup;;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 作业单明细
 * wms_inner_job_order_det
 * @author Dylan
 * @date 2021-12-08 18:11:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wms_inner_job_order_det")
public class WmsInnerJobOrderDet extends ValidGroup implements Serializable {
    /**
     * 作业单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "作业单明细ID")
    @Excel(name = "作业单明细ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    /**
     * 作业单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    @Excel(name = "作业单ID", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 核心单据编码
     */
    @ApiModelProperty(name="coreSourceOrderCode",value = "核心单据编码")
    @Excel(name = "核心单据编码", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_order_code")
    private String coreSourceOrderCode;

    /**
     * 来源单据编码
     */
    @ApiModelProperty(name="sourceOrderCode",value = "来源单据编码")
    @Excel(name = "来源单据编码", height = 20, width = 30,orderNum="")
    @Column(name = "source_order_code")
    private String sourceOrderCode;

    /**
     * 核心明细ID
     */
    @ApiModelProperty(name="coreSourceId",value = "核心明细ID")
    @Excel(name = "核心明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_id")
    private Long coreSourceId;

    /**
     * 来源明细ID
     */
    @ApiModelProperty(name="sourceId",value = "来源明细ID")
    @Excel(name = "来源明细ID", height = 20, width = 30,orderNum="")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 行号
     */
    @ApiModelProperty(name="lineNumber",value = "行号")
    @Excel(name = "行号", height = 20, width = 30,orderNum="")
    @Column(name = "line_number")
    private String lineNumber;

    /**
     * 移入库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "移入库位ID")
    @Excel(name = "移入库位ID", height = 20, width = 30,orderNum="")
    @Column(name = "in_storage_id")
    private Long inStorageId;

    /**
     * 移出库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "移出库位ID")
    @Excel(name = "移出库位ID", height = 20, width = 30,orderNum="")
    @Column(name = "out_storage_id")
    private Long outStorageId;

    /**
     * 库存状态ID
     */
    @ApiModelProperty(name="inventoryStatusId",value = "库存状态ID")
    @Excel(name = "库存状态ID", height = 20, width = 30,orderNum="")
    @Column(name = "inventory_status_id")
    private Long inventoryStatusId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="")
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="")
    @Column(name = "batch_code")
    private String batchCode;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="")
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 分配数量
     */
    @ApiModelProperty(name="distributionQty",value = "分配数量")
    @Excel(name = "分配数量", height = 20, width = 30,orderNum="")
    @Column(name = "distribution_qty")
    private BigDecimal distributionQty;

    /**
     * 实际数量
     */
    @ApiModelProperty(name="actualQty",value = "实际数量")
    @Excel(name = "实际数量", height = 20, width = 30,orderNum="")
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 作业开始时间
     */
    @ApiModelProperty(name="workStartTime",value = "作业开始时间")
    @Excel(name = "作业开始时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "work_start_time")
    private Date workStartTime;

    /**
     * 作业结束时间
     */
    @ApiModelProperty(name="workEndTime",value = "作业结束时间")
    @Excel(name = "作业结束时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "work_end_time")
    private Date workEndTime;

    /**
     * 行状态(1-待分配、2-待作业、3-完成)
     */
    @ApiModelProperty(name="lineStatus",value = "行状态(1-待分配、2-待作业、3-完成)")
    @Excel(name = "行状态(1-待分配、2-待作业、3-完成)", height = 20, width = 30,orderNum="")
    @Column(name = "line_status")
    private Byte lineStatus;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="")
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
    @ApiModelProperty(name="orgId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="")
    @Column(name = "org_id")
    private Long orgId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    List<WmsInnerInventoryDto> wmsInnerInventory = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}
