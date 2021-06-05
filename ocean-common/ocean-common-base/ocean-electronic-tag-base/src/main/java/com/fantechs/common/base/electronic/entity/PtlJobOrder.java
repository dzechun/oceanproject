package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * PTL-作业单
 * ptl_job_order
 * @author 86178
 * @date 2021-06-01 17:06:57
 */
@Data
@Table(name = "ptl_job_order")
public class PtlJobOrder extends ValidGroup implements Serializable {
    /**
     * 作业单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "作业单ID")
    @Excel(name = "作业单ID", height = 20, width = 30,orderNum="") 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "任务单号")
    @Excel(name = "任务单号", height = 20, width = 30,orderNum="")
    @Column(name = "job_order_code")
    private String jobOrderCode;

    /**
     * 作业类型(1-上架 2-分拣)
     */
    @ApiModelProperty(name="jobOrderType",value = "作业类型(1-上架 2-分拣)")
    @Excel(name = "作业类型(1-上架 2-分拣)", height = 20, width = 30,orderNum="") 
    @Column(name = "job_order_type")
    private Byte jobOrderType;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "拣货单号")
    @Excel(name = "拣货单号", height = 20, width = 30,orderNum="")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 发货单号
     */
    @ApiModelProperty(name="despatchOrderCode",value = "发货单号")
    @Excel(name = "发货单号", height = 20, width = 30,orderNum="")
    @Column(name = "despatch_order_code")
    private String despatchOrderCode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_name")
    private String warehouseName;

    /**
     * 作业人员ID
     */
    @ApiModelProperty(name="workerUserId",value = "作业人员ID")
    @Excel(name = "作业人员ID", height = 20, width = 30,orderNum="") 
    @Column(name = "worker_user_id")
    private Long workerUserId;

    /**
     * 作业人员名称
     */
    @ApiModelProperty(name="workerUserName",value = "作业人员名称")
    @Excel(name = "作业人员名称", height = 20, width = 30,orderNum="") 
    @Column(name = "worker_user_name")
    private String workerUserName;

    /**
     * 单据状态(1-待激活、2-已激活、3-完成、4-异常)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待激活、2-已激活、3-完成、4-异常)")
    @Excel(name = "单据状态(1-待激活、2-已激活、3-完成、4-异常)", height = 20, width = 30,orderNum="") 
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 是否已打印条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifAlreadyPrint",value = "是否已打印条码(0-否 1-是)")
    @Excel(name = "是否已打印条码(0-否 1-是)", height = 20, width = 30,orderNum="") 
    @Column(name = "if_already_print")
    private Byte ifAlreadyPrint;

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

    private static final long serialVersionUID = 1L;
}