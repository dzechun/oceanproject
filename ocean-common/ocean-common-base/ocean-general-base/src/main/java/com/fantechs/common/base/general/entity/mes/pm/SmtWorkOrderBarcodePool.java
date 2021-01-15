package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 工单任务池表
 * smt_work_order_barcode_pool
 * @author 18358
 * @date 2020-11-23 15:44:09
 */
@Data
@Table(name = "smt_work_order_barcode_pool")
public class SmtWorkOrderBarcodePool implements Serializable {
    /**
     * 工单任务池ID
     */
    @ApiModelProperty(name="workOrderBarcodePoolId",value = "工单任务池ID")
    @Id
    @Column(name = "work_order_barcode_pool_id")
    private Long workOrderBarcodePoolId;

    /**
     * 任务单号
     */
    @ApiModelProperty(name="taskCode",value = "任务单号")
    @Excel(name = "任务单号", height = 20, width = 30,orderNum="1")
    @Column(name = "task_code")
    private String taskCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="2")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工单流转卡任务池ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "工单流转卡任务池ID")
    @Excel(name = "工单流转卡任务池ID", height = 20, width = 30,orderNum="3")
    @Column(name = "work_order_card_pool_id")
    private Long workOrderCardPoolId;

    /**
     * 编码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "编码规则ID")
    @Excel(name = "编码规则ID", height = 20, width = 30,orderNum="4")
    @Column(name = "barcode_rule_id")
    private Long barcodeRuleId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    @Excel(name = "条码", height = 20, width = 30,orderNum="5")
    private String barcode;

    /**
     * 任务状态(0-待投产 1-投产中 2-已完成)
     */
    @ApiModelProperty(name="taskStatus",value = "任务状态(0-待投产 1-投产中 2-已完成)")
    @Excel(name = "任务状态(0-待投产 1-投产中 2-已完成)", height = 20, width = 30,orderNum="6")
    @Column(name = "task_status")
    private Byte taskStatus;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="7")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    private static final long serialVersionUID = 1L;
}
