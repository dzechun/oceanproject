package com.fantechs.common.base.general.entity.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 工序报工表
 * mes_process_report_work
 * @author 86178
 * @date 2021-03-19 09:52:30
 */
@Data
@Table(name = "mes_process_report_work")
public class MesProcessReportWork extends ValidGroup implements Serializable {
    /**
     * 工序报工ID
     */
    @ApiModelProperty(name="processReportWorkId",value = "工序报工ID")
    @Id
    @Column(name = "process_report_work_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long processReportWorkId;

    /**
     * 工序报工单号
     */
    @ApiModelProperty(name="processReportWorkCode",value = "工序报工单号")
    @Excel(name = "工序报工单号", height = 20, width = 30,orderNum="3")
    @Column(name = "process_report_work_code")
    private String processReportWorkCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 班组id
     */
    @ApiModelProperty(name="teamId",value = "班组id")
    @Column(name = "team_id")
    private Long teamId;

    /**
     * 报工员工id
     */
    @ApiModelProperty(name="staffId",value = "报工员工id")
    @Column(name = "staff_id")
    private Long staffId;

    /**
     * 报工类型（1-手动报工类型）
     */
    @ApiModelProperty(name="status",value = "报工类型（1-手动报工类型）")
    @Excel(name = "报工类型（1-手动报工类型）", height = 20, width = 30,orderNum="2")
    private Byte type;

    /**
     * 报工状态(0-已报工 1-已完工)
     */
    @ApiModelProperty(name="status",value = "报工状态(0-已报工 1-已完工)")
    @Excel(name = "报工状态(0-已报工 1-已完工)", height = 20, width = 30,orderNum="13")
    private Byte status;

    /**
     * 本次报工数量
     */
    @ApiModelProperty(name="quantity",value = "本次报工数量")
    @Excel(name = "本次报工数量", height = 20, width = 30,orderNum="11")
    private BigDecimal quantity;

    /**
     * 报工开始时间
     */
    @Column(name = "start_time")
    @ApiModelProperty(name="startTime" ,value="报工开始时间")
    @Excel(name = "报工开始时间", height = 20, width = 30,orderNum="14",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 报工结束时间
     */
    @Column(name = "end_time")
    @ApiModelProperty(name="plannedEndTime" ,value="报工结束时间")
    @Excel(name = "报工结束时间", height = 20, width = 30,orderNum="15",exportFormat = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="12")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
     * 工序和报工人员配置（0-两者必须关联 1-可以不关联）
     */
    @Transient
    @ApiModelProperty(name="工序和报工人员配置（0-两者必须关联 1-可以不关联）",value = "工序和报工人员配置（0-两者必须关联 1-可以不关联）")
    private int processAndStaff;

    private static final long serialVersionUID = 1L;
}