package com.fantechs.common.base.general.entity.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Table(name = "mes_pm_master_plan")
@Data
public class MesPmMasterPlan implements Serializable {
    /**
    * 总计划ID
    */
    @ApiModelProperty(value = "总计划ID",example = "总计划ID")
    @Column(name = "master_plan_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Excel(name = "总计划ID")
    private Long masterPlanId;

    /**
    * 总计划编码
    */
    @ApiModelProperty(value = "总计划编码",example = "总计划编码")
    @Column(name = "master_plan_code")
    @Excel(name = "总计划编码")
    private String masterPlanCode;

    /**
    * 工单ID
    */
    @ApiModelProperty(value = "工单ID",example = "工单ID")
    @Column(name = "work_order_id")
    @Excel(name = "工单ID")
    private Long workOrderId;

    /**
     * 产线ID
     */
    @ApiModelProperty(value = "产线ID",example = "产线ID")
    @Column(name = "pro_line_id")
    @Excel(name = "产线ID")
    private Long proLineId;

    /**
    * 生产总数
    */
    @ApiModelProperty(value = "生产总数",example = "生产总数")
    @Column(name = "product_qty")
    @Excel(name = "生产总数")
    private java.math.BigDecimal productQty;

    /**
    * 已排产数量
    */
    @ApiModelProperty(value = "已排产数量",example = "已排产数量")
    @Column(name = "scheduled_qty")
    @Excel(name = "已排产数量")
    private java.math.BigDecimal scheduledQty;

    /**
    * 已排产数量
    */
    @ApiModelProperty(value = "未排产数量",example = "未排产数量")
    @Column(name = "no_schedule_qty")
    @Excel(name = "未排产数量")
    private java.math.BigDecimal noScheduleQty;

    /**
    * 完成数量
    */
    @ApiModelProperty(value = "完成数量",example = "完成数量")
    @Column(name = "finished_qty")
    @Excel(name = "完成数量")
    private java.math.BigDecimal finishedQty;

    /**
    * 计划开工时间
    */
    @ApiModelProperty(value = "计划开工时间",example = "计划开工时间")
    @Column(name = "planed_start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "计划开工时间")
    private java.util.Date planedStartDate;

    /**
    * 计划完工时间
    */
    @ApiModelProperty(value = "计划完工时间",example = "计划完工时间")
    @Column(name = "planed_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "计划完工时间")
    private java.util.Date planedEndDate;

    /**
    * 实际开工时间
    */
    @ApiModelProperty(value = "实际开工时间",example = "实际开工时间")
    @Column(name = "actual_start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "实际开工时间")
    private java.util.Date actualStartDate;

    /**
    * 实际完工时间
    */
    @ApiModelProperty(value = "实际完工时间",example = "实际完工时间")
    @Column(name = "actual_end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "实际完工时间")
    private java.util.Date actualEndDate;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "organization_id")
    @Excel(name = "组织代码id")
    private Long organizationId;

    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",example = "备注")
    @Excel(name = "备注")
    private String remark;

    /**
    * 逻辑删除（0、删除 1、正常）
    */
    @ApiModelProperty(value = "逻辑删除（0、删除 1、正常）",example = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    @Excel(name = "逻辑删除（0、删除 1、正常）")
    private Byte isDelete;

    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID",example = "创建人ID")
    @Column(name = "create_user_id")
    @Excel(name = "创建人ID")
    private Long createUserId;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",example = "创建时间")
    @Column(name = "create_time")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private java.util.Date createTime;

    /**
    * 修改人ID
    */
    @ApiModelProperty(value = "修改人ID",example = "修改人ID")
    @Column(name = "modified_user_id")
    @Excel(name = "修改人ID")
    private Long modifiedUserId;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",example = "修改时间")
    @Column(name = "modified_time")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private java.util.Date modifiedTime;

}