package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上架单
 * wms_inner_job_order
 * @author mr.lei
 * @date 2021-05-06 10:03:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wms_inner_job_order")
public class WmsInnerJobOrder extends ValidGroup implements Serializable {
    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID")
    @Excel(name = "上架单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * ASN单ID
     */
    @ApiModelProperty(name="sourceOrderId",value = "ASN单ID")
    @Excel(name = "ASN单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_order_id")
    private Long sourceOrderId;

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
     * 单据类型ID
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    @Excel(name = "单据类型ID", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type_id")
    private Long orderTypeId;

    /**
     * 工作区ID
     */
    @ApiModelProperty(name="workingAreaId",value = "工作区ID")
    @Excel(name = "工作区ID", height = 20, width = 30,orderNum="") 
    @Column(name = "working_area_id")
    private Long workingAreaId;

    /**
     * 工作人员ID
     */
    @ApiModelProperty(name="workerId",value = "工作人员ID")
    @Excel(name = "工作人员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "worker_id")
    private Long workerId;

    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "作业单号")
    @Excel(name = "作业单号", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_code")
    private String jobOrderCode;

    /**
     * 作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)
     */
    @ApiModelProperty(name="jobOrderType",value = "作业类型(1-加工拣货 2-移位 3-上架 4-拣货 5-补货)")
    @Excel(name = "作业类型", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_type")
    private Byte jobOrderType;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "相关单号")
    @Excel(name = "相关单号", height = 20, width = 30,orderNum="") 
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 计划数量
     */
    @ApiModelProperty(name="planQty",value = "计划数量")
    @Excel(name = "计划数量", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_qty")
    private BigDecimal planQty;

    /**
     * 拣货数量
     */
    @ApiModelProperty(name="actualQty",value = "拣货数量")
    @Excel(name = "拣货数量", height = 20, width = 30,orderNum="") 
    @Column(name = "actual_qty")
    private BigDecimal actualQty;

    /**
     * 分配数量
     */
    @Transient
    @ApiModelProperty(name="distributionQty",value = "分配数量")
    @Excel(name = "分配数量", height = 20, width = 30,orderNum="")
    private BigDecimal distributionQty;

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
    @ApiModelProperty(name="workEndtTime",value = "作业结束时间")
    @Excel(name = "作业结束时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "work_endt_time")
    private Date workEndtTime;

    /**
     * 单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待分配2-分配中 3-待作业 4-作业中 5-完成 6-待激活)")
    @Excel(name = "单据状态(1-待作业 2-作业中 3-作业完成)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

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

    @Transient
    @ApiModelProperty("明细")
    private List<WmsInnerJobOrderDet> wmsInPutawayOrderDets;

    /**
     * 车间管理模块栈板表ID
     */
    @Transient
    @ApiModelProperty(name = "productPalletId",value = "车间管理模块栈板表ID")
    private Long productPalletId;

    /**
     * TYPE-1 生产待作业拣货单 type=0
     */
    @Transient
    private Integer type;

    private static final long serialVersionUID = 1L;
}