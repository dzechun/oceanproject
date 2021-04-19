package com.fantechs.common.base.general.entity.mes.pm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Table(name = "mes_pm_explain_process_plan")
@Data
public class MesPmExplainProcessPlan implements Serializable {
    /**
    * 执行工序计划ID
    */
    @ApiModelProperty(value = "执行工序计划ID",example = "执行工序计划ID")
    @Column(name = "explain_process_plan_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    @Excel(name = "执行工序计划ID")
    private Long explainProcessPlanId;

    /**
    * 执行工序计划编码
    */
    @ApiModelProperty(value = "执行工序计划编码",example = "执行工序计划编码")
    @Column(name = "explain_process_plan_code")
    @Excel(name = "执行工序计划编码")
    private String explainProcessPlanCode;

    /**
    * 执行计划ID
    */
    @ApiModelProperty(value = "执行计划ID",example = "执行计划ID")
    @Column(name = "explain_plan_id")
    @Excel(name = "执行计划ID")
    private Long explainPlanId;

    /**
    * 工序ID
    */
    @ApiModelProperty(value = "工序ID",example = "工序ID")
    @Column(name = "process_id")
    @Excel(name = "工序ID")
    private Long processId;

    /**
     * 是否报工扫描（0、否 1、是）
     */
    @ApiModelProperty(value = "是否报工扫描（0、否 1、是）",example = "是否报工扫描（0、否 1、是）")
    @Column(name = "is_job_scan")
    @Excel(name = "是否报工扫描（0、否 1、是）")
    private Byte isJobScan;

    /**
     * 是否开工扫描（0、否 1、是）
     */
    @ApiModelProperty(value = "是否开工扫描（0、否 1、是）",example = "是否开工扫描（0、否 1、是）")
    @Column(name = "is_start_scan")
    @Excel(name = "是否开工扫描（0、否 1、是）")
    private Byte isStartScan;

    /**
     * 是否品质确认（0、否 1、是）
     */
    @ApiModelProperty(value = "是否品质确认（0、否 1、是）",example = "是否品质确认（0、否 1、是）")
    @Column(name = "is_quality")
    @Excel(name = "是否品质确认（0、否 1、是）")
    private Byte isQuality;

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
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "计划开工时间")
    private java.util.Date planedStartDate;

    /**
    * 计划完工时间
    */
    @ApiModelProperty(value = "计划完工时间",example = "计划完工时间")
    @Column(name = "planed_end_date")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "计划完工时间")
    private java.util.Date planedEndDate;

    /**
    * 实际开工时间
    */
    @ApiModelProperty(value = "实际开工时间",example = "实际开工时间")
    @Column(name = "actual_start_date")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "实际开工时间")
    private java.util.Date actualStartDate;

    /**
    * 实际完工时间
    */
    @ApiModelProperty(value = "实际完工时间",example = "实际完工时间")
    @Column(name = "actual_end_date")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm")
    @Excel(name = "实际完工时间")
    private java.util.Date actualEndDate;

    /**
    * 组织代码id
    */
    @ApiModelProperty(value = "组织代码id",example = "组织代码id")
    @Column(name = "org_id")
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