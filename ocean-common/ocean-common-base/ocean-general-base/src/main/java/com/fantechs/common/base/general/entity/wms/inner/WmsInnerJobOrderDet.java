package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上架单明细
 * wms_inner_job_order_det
 * @author mr.lei
 * @date 2021-05-06 11:04:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wms_inner_job_order_det")
public class WmsInnerJobOrderDet extends ValidGroup implements Serializable {
    /**
     * 上架单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "上架单明细ID")
    @Excel(name = "上架单明细ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "job_order_det_id")
    private Long jobOrderDetId;

    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID")
    @Excel(name = "上架单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 来源明细id
     */
    @ApiModelProperty(name="sourceDetId",value = "来源明细id")
    @Excel(name = "来源明细id", height = 20, width = 30,orderNum="")
    @Column(name = "source_det_id")
    private Long sourceDetId;

    /**
     * 货主ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主ID")
    @Excel(name = "货主ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

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
     * 月台id
     */
    @ApiModelProperty(name = "platformId",value = "月台")
    @Column(name = "platform_id")
    private Long platformId;

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
     * 包装单位
     */
    @ApiModelProperty(name="packingUnitName",value = "包装单位")
    @Excel(name = "包装单位", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_unit_name")
    private String packingUnitName;

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
     * 拣货数量
     */
    @ApiModelProperty(name="actualQty",value = "拣货数量")
    @Excel(name = "拣货数量", height = 20, width = 30,orderNum="") 
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 托盘号
     */
    @ApiModelProperty(name="palletCode",value = "托盘号")
    @Excel(name = "托盘号", height = 20, width = 30,orderNum="") 
    @Column(name = "pallet_code")
    private String palletCode;

    /**
     * 收货日期
     */
    @ApiModelProperty(name="receivingDate",value = "收货日期")
    @Excel(name = "收货日期", height = 20, width = 30,orderNum="") 
    @Column(name = "receiving_date")
    private Date receivingDate;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30,orderNum="") 
    @Column(name = "production_date")
    private Date productionDate;

    /**
     * 过期日期
     */
    @ApiModelProperty(name="expiredDate",value = "过期日期")
    @Excel(name = "过期日期", height = 20, width = 30,orderNum="") 
    @Column(name = "expired_date")
    private Date expiredDate;

    /**
     * 作业开始时间
     */
    @ApiModelProperty(name="workStartTime",value = "作业开始时间")
    @Excel(name = "作业开始时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "work_start_time")
    private Date workStartTime;

    /**
     * 作业结束时间
     */
    @ApiModelProperty(name="workEndTime",value = "作业结束时间")
    @Excel(name = "作业结束时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "work_end_time")
    private Date workEndTime;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30,orderNum="") 
    @Column(name = "batch_code")
    private String batchCode;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 单据状态(1.待分配、2.分配中、3.已分配、4.作业中、5.作业完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1.待分配、2.分配中、3.已分配、4.作业中、5.作业完成)")
    @Excel(name = "单据状态(1.待分配、2.分配中、3.已分配、4.作业中、5.作业完成)", height = 20, width = 30,orderNum="")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 移位状态(1-待作业 2-拣货中 3-上架中 4-已完成)
     */
    @ApiModelProperty(name="shiftStorageStatus",value = "移位状态(1-待作业 2-拣货中 3-上架中 4-已完成)")
    @Excel(name = "移位状态(1-待作业 2-拣货中 3-上架中 4-已完成)", height = 20, width = 30,orderNum="")
    @Column(name = "shift_storage_status")
    private Byte shiftStorageStatus;

    private static final long serialVersionUID = 1L;
}