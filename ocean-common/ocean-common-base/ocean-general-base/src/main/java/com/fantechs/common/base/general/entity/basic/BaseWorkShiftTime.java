package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 班次时间表
 * @date 2021-01-04 14:31:42
 */
@Data
@Table(name = "base_work_shift_time")
public class BaseWorkShiftTime extends ValidGroup implements Serializable {
    /**
     * 班次时间id
     */
    @ApiModelProperty(name="workShiftTimeId",value = "班次时间id")
    @Id
    @Column(name = "work_shift_time_id")
    private Long workShiftTimeId;

    /**
     * 班次id
     */
    @ApiModelProperty(name="workShiftId",value = "班次id")
    @Excel(name = "班次id", height = 20, width = 30)
    @Column(name = "work_shift_id")
    private Long workShiftId;

    /**
     * 开始时间
     */
    @ApiModelProperty(name="startTime",value = "开始时间")
    @Excel(name = "开始时间", height = 20, width = 30,orderNum="4")
    @Column(name = "start_time")
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "HH:mm")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="workShiftDesc",value = "结束时间")
    @Excel(name = "结束时间", height = 20, width = 30,orderNum="5")
    @Column(name = "end_time")
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "HH:mm")
    private Date endTime;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,replace = {"无效_0", "有效_1"})
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30)
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30)
    @Column(name = "org_id")
    private Long organizationId;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30)
    @Column(name = "is_delete")
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}
