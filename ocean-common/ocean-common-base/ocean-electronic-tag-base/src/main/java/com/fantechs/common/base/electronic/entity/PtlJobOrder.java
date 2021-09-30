package com.fantechs.common.base.electronic.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

;
;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    @Column(name = "job_order_id")
    private Long jobOrderId;

    /**
     * 作业单号
     */
    @ApiModelProperty(name="jobOrderCode",value = "任务单号")
    @Excel(name = "任务单号", height = 20, width = 30,orderNum="1")
    @Column(name = "job_order_code")
    private String jobOrderCode;

    /**
     * 作业类型(1-上架 2-分拣)
     */
    @ApiModelProperty(name="jobOrderType",value = "作业类型(1-上架 2-分拣)")
    @Column(name = "job_order_type")
    private Byte jobOrderType;

    /**
     * 相关单号
     */
    @ApiModelProperty(name="relatedOrderCode",value = "拣货单号")
    @Excel(name = "拣货单号", height = 20, width = 30,orderNum="2")
    @Column(name = "related_order_code")
    private String relatedOrderCode;

    /**
     * 发货单号
     */
    @ApiModelProperty(name="despatchOrderCode",value = "发货单号")
    @Column(name = "despatch_order_code")
    private String despatchOrderCode;

    /**
     * 是否加急(0-否 1-是)
     */
    @ApiModelProperty(name="ifUrgent",value = "是否加急(0-否 1-是)")
    @Column(name = "if_urgent")
    private Byte ifUrgent;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    @Excel(name = "仓库名称", height = 20, width = 30,orderNum="5")
    @Column(name = "warehouse_name")
    private String warehouseName;

    /**
     * 周转工具ID
     */
    @ApiModelProperty(name="vehicleId",value = "周转工具ID")
    @Column(name = "vehicle_id")
    private Long vehicleId;

    /**
     * 作业人员ID
     */
    @ApiModelProperty(name="workerUserId",value = "作业人员ID")
    @Column(name = "worker_user_id")
    private Long workerUserId;

    /**
     * 作业人员名称
     */
    @ApiModelProperty(name="workerUserName",value = "作业人员")
    @Excel(name = "作业人员", height = 20, width = 30,orderNum="6")
    @Column(name = "worker_user_name")
    private String workerUserName;

    /**
     * 单据状态(1-待激活、2-已激活、3-完成、4-异常)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待激活、2-已激活、3-完成、4-挂起 5-已取消 6-已复核)")
    @Excel(name = "单据状态(1-待激活、2-已激活、3-完成、4-挂起 5-已取消 6-已复核)", height = 20, width = 30,orderNum="7")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 退拣状态(0-未退拣 1-已退拣)
     */
    @ApiModelProperty(name="pickBackStatus",value = "退拣状态(0-未退拣 1-已退拣)")
    @Column(name = "pick_back_status")
    private Byte pickBackStatus;

    /**
     * 是否已打印条码(0-否 1-是)
     */
    @ApiModelProperty(name="ifAlreadyPrint",value = "是否已打印条码(0-否 1-是)")
    @Column(name = "if_already_print")
    private Byte ifAlreadyPrint;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "区域")
    @Excel(name = "区域", height = 20, width = 30,orderNum="4")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    @ApiModelProperty(name="option1",value = "灯颜色 0-红色 1-绿色 2-黄色")
    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}