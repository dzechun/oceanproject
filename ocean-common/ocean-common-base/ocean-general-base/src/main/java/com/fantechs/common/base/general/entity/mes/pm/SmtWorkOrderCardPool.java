package com.fantechs.common.base.general.entity.mes.pm;

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


/**
 * 工单流转卡任务池表
 * base_work_order_card_pool
 * @author 18358
 * @date 2020-11-21 16:51:43
 */
@Data
@Table(name = "base_work_order_card_pool")
public class SmtWorkOrderCardPool extends ValidGroup implements Serializable {
    /**
     * 工单流转卡任务池ID
     */
    @ApiModelProperty(name="workOrderCardPoolId",value = "工单流转卡任务池ID")
    @Id
    @Column(name = "work_order_card_pool_id")
    @NotNull(groups = ValidGroup.update.class,message = "工单流转卡任务池ID不能为空")
    @GeneratedValue(strategy= GenerationType.IDENTITY,generator = "JDBC")
    private Long workOrderCardPoolId;

    /**
     * 父级ID
     */
    @ApiModelProperty(name="parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;

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
     * 条码规则ID
     */
    @ApiModelProperty(name="barcodeRuleId",value = "条码规则ID")
    @Column(name = "barcode_rule_id")
    private Long barcodeRuleId;

    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId",value = "工单流转卡编码")
    @Excel(name = "工单流转卡编码", height = 20, width = 30,orderNum="3")
    @Column(name = "work_order_card_id")
    private String workOrderCardId;

    /**
     * 流转卡状态(0-待投产 1-投产中 2-已完成)
     */
    @ApiModelProperty(name="cardStatus",value = "流转卡状态(0-待投产 1-投产中 2-已完成)")
    @Excel(name = "流转卡状态(0-待投产 1-投产中 2-已完成)", height = 20, width = 30,orderNum="4")
    @Column(name = "card_status")
    private Byte cardStatus;

    /**
     * 流程卡类型（1、工单流转卡 2、部件流转卡）
     */
    @ApiModelProperty(name="type",value = "流程卡类型（1、工单流转卡 2、部件流转卡）")
    private Byte type;

    /**
     * 流程卡投产数
     */
    @ApiModelProperty(name="outPutQty",value = "流程卡投产数")
    @Column(name = "out_put_qty")
    private BigDecimal outPutQty;

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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="5",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
