package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 日历表
 * base_calendar
 * @author 53203
 * @date 2020-12-21 18:15:04
 */
@Data
@Table(name = "base_calendar")
public class BaseCalendar extends ValidGroup implements Serializable {
    /**
     * 日历ID
     */
    @ApiModelProperty(name="calendarId",value = "日历ID")
    @Excel(name = "日历ID", height = 20, width = 30)
    @Id
    @Column(name = "calendar_id")
    @NotNull(groups = update.class,message = "日历id不能为空")
    private Long calendarId;

    /**
     * 产线ID
     */
    @ApiModelProperty(name="proLineId",value = "产线ID")
    @Excel(name = "产线ID", height = 20, width = 30)
    @Column(name = "pro_line_id")
    @NotNull(message = "产线id不能为空")
    private Long proLineId;

    /**
     * 日历编码
     */
    @ApiModelProperty(name="calendarCode",value = "日历编码")
    @Excel(name = "日历编码", height = 20, width = 30)
    @Column(name = "calendar_code")
    private String calendarCode;

    /**
     * 日历名称
     */
    @ApiModelProperty(name="calendarName",value = "日历名称")
    @Excel(name = "日历名称", height = 20, width = 30)
    @Column(name = "calendar_name")
    private String calendarName;

    /**
     * 日历描述
     */
    @ApiModelProperty(name="calendarDesc",value = "日历描述")
    @Excel(name = "日历描述", height = 20, width = 30)
    @Column(name = "calendar_desc")
    private String calendarDesc;

    /**
     * 日期-年月
     */
    @ApiModelProperty(name="day",value = "日期-年月")
    @Excel(name = "日期-年月", height = 20, width = 30)
    @NotNull(message = "日期不能为空")
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
     * 状态(0-无效 1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0-无效 1有效)")
    @Excel(name = "状态(0-无效 1有效)", height = 20, width = 30)
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
     * 逻辑删除(0-删除 1-正常)
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除(0-删除 1-正常)")
    @Excel(name = "逻辑删除(0-删除 1-正常)", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;


    private static final long serialVersionUID = 1L;
}