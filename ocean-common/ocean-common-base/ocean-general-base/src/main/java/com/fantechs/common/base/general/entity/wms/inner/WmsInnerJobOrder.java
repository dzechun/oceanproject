package com.fantechs.common.base.general.entity.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 作业单
 * wms_inner_job_order
 * @author Dylan
 * @date 2021-12-08 18:11:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wms_inner_job_order")
public class WmsInnerJobOrder extends ValidGroup implements Serializable {
    /**
     * 作业单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    @Excel(name = "作业单ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="coreSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Excel(name = "核心系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "core_source_sys_order_type_code")
    private String coreSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Excel(name = "来源系统单据类型编码", height = 20, width = 30,orderNum="")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 来源大类(1-系统下推 2-自建 3-第三方系统)
     */
    @ApiModelProperty(name="sourceBigType",value = "来源大类(1-系统下推 2-自建 3-第三方系统)")
    @Column(name = "source_big_type")
    private Byte sourceBigType;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="")
    @Column(name = "warehouse_id")
    private Long warehouseId;

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
     * 作业类型(1-上架 2-拣货 3-移位)
     */
    @ApiModelProperty(name="jobOrderType",value = "作业类型(1-上架 2-拣货 3-移位)")
    @Excel(name = "作业类型(1-上架 2-拣货 3-移位)", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_type")
    private Byte jobOrderType;

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
    @ApiModelProperty(name="workEndtTime",value = "作业结束时间")
    @Excel(name = "作业结束时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "work_endt_time")
    private Date workEndtTime;

    /**
     * 单据状态(1-待分配、2-分配中、3-待作业、4-作业中、5-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待分配、2-分配中、3-待作业、4-作业中、5-完成)")
    @Excel(name = "单据状态(1-待分配、2-分配中、3-待作业、4-作业中、5-完成)", height = 20, width = 30,orderNum="")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
     * 上架或拣货单明细
     */
    @Transient
    @ApiModelProperty("上架或拣货单明细")
    private List<WmsInnerJobOrderDet> wmsInPutawayOrderDets;

    /**
     * 车间管理模块栈板表ID
     */
    @Transient
    @ApiModelProperty(name = "productPalletId",value = "车间管理模块栈板表ID")
    private Long productPalletId;

    private static final long serialVersionUID = 1L;
}
