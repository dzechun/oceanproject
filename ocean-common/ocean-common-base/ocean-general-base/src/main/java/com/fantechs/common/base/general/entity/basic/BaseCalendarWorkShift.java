package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 日历班次关系表
 * base_calendar_work_shift
 * @author 53203
 * @date 2020-12-21 18:15:04
 */
@Data
@Table(name = "base_calendar_work_shift")
public class BaseCalendarWorkShift extends ValidGroup implements Serializable {
    /**
     * 日历班次关系ID
     */
    @ApiModelProperty(name="calendarWorkShiftId",value = "日历班次关系ID")
    @Excel(name = "日历班次关系ID", height = 20, width = 30)
    @Id
    @Column(name = "calendar_work_shift_id")
    @NotNull(groups = update.class,message = "日历班次关系ID不能为空")
    private Long calendarWorkShiftId;

    /**
     * 班次ID
     */
    @ApiModelProperty(name="workShiftId",value = "班次ID")
    @Excel(name = "班次ID", height = 20, width = 30)
    @Column(name = "work_shift_id")
    @NotNull(message = "班次id不能为空")
    private Long workShiftId;

    /**
     * 日历ID
     */
    @ApiModelProperty(name="calendarId",value = "日历ID")
    @Excel(name = "日历ID", height = 20, width = 30)
    @Column(name = "calendar_id")
    private Long calendarId;

    /**
     * 产线ID
     */
    @Transient
    @ApiModelProperty(name="proLineId",value = "产线ID")
    private Long proLineId;

    /**
     * 日期-天
     */
    @ApiModelProperty(name="proLineId",value = "日期-天")
    @Excel(name = "日期-天", height = 20, width = 30)
    @Column(name = "day")
    @NotNull(message = "日期不能为空")
    private Long day;

    /**
     * 日期-年月
     */
    @Transient
    @ApiModelProperty(name="date",value = "日期-年月")
    private String date;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
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
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30)
    private Byte status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(name="createUserId",value = "创建人ID")
    @Excel(name = "创建人ID", height = 20, width = 30)
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @Excel(name = "创建时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    @Excel(name = "修改人ID", height = 20, width = 30)
    @Column(name = "modified_user_id")
    private Long modifiedUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(name="modifiedTime",value = "修改时间")
    @Excel(name = "修改时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除(0、删除 1、正常)
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除(0、删除 1、正常)")
    @Excel(name = "逻辑删除(0、删除 1、正常)", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}