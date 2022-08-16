package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 日计划履历表
 * mes_pm_ht_daily_plan
 * @author 81947
 * @date 2021-06-03 20:02:21
 */
@Data
@Table(name = "mes_pm_ht_daily_plan")
public class MesPmHtDailyPlan extends ValidGroup implements Serializable {
    /**
     * 日计划履历ID
     */
    @ApiModelProperty(name="htDailyPlanId",value = "日计划履历ID")
    @Excel(name = "日计划履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_daily_plan_id")
    private Long htDailyPlanId;

    /**
     * 日计划ID
     */
    @ApiModelProperty(name="dailyPlanId",value = "日计划ID")
    @Excel(name = "日计划ID", height = 20, width = 30,orderNum="") 
    @Column(name = "daily_plan_id")
    private Long dailyPlanId;

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
    @ApiModelProperty(name="scheduleQty",value = "排产数量")
    @Excel(name = "排产数量", height = 20, width = 30,orderNum="") 
    @Column(name = "schedule_qty")
    private BigDecimal scheduleQty;

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
    @Excel(name = "计划时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
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
}