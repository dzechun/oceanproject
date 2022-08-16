package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * 日计划
 * mes_pm_daily_plan
 * @author 81947
 * @date 2021-06-02 17:16:18
 */
@Data
@Table(name = "mes_pm_daily_plan")
public class MesPmDailyPlan extends ValidGroup implements Serializable {
    /**
     * 日计划ID
     */
    @ApiModelProperty(name="dailyPlanId",value = "日计划ID")
    @Excel(name = "日计划ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "daily_plan_id")
    private Long dailyPlanId;

    /**
     * 序号
     */
    @ApiModelProperty(name="seqNum",value = "序号")
    @Excel(name = "序号", height = 20, width = 30)
    @Column(name = "seq_num")
    private String seqNum;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 排产数量
     */
    @ApiModelProperty(name="scheduledQty",value = "排产数量")
    @Excel(name = "排产数量", height = 20, width = 30,orderNum="") 
    @Column(name = "schedule_qty")
    private BigDecimal scheduledQty;

    /**
     * 完成数量
     */
    @ApiModelProperty(name="finishedQty",value = "完成数量")
    @Excel(name = "完成数量", height = 20, width = 30,orderNum="") 
    @Column(name = "finished_qty")
    private BigDecimal finishedQty;

    /**
     * 计划时间
     */
    @ApiModelProperty(name="planTime",value = "计划时间")
    @Excel(name = "计划时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd")
    @JSONField(format ="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "plan_time")
    private Date planTime;

    /**
     * 是否插单(0-否 1-是)
     */
    @ApiModelProperty(name="ifOrderInserting",value = "是否插单(0-否 1-是)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="")
    @Column(name = "if_order_inserting")
    private Byte ifOrderInserting;


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

    @Transient
    @ApiModelProperty(name = "planDate",value = "计划日期（yyyy-MM-dd）")
    private String planDate;
}