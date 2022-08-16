package com.fantechs.common.base.general.entity.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.support.ValidGroup;;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 员工工序信息表
 * base_staff_process
 * @author 53203
 * @date 2021-01-16 20:25:42
 */
@Data
@Table(name = "base_staff_process")
public class BaseStaffProcess extends ValidGroup implements Serializable {
    /**
     * 员工工序关系id
     */
    @ApiModelProperty(name="staffProcessId",value = "员工工序关系id")
    @Excel(name = "员工工序关系id", height = 20, width = 30)
    @Id
    @Column(name = "staff_process_id")
    @NotNull(groups = update.class,message = "员工工序关系id不能为空")
    private Long staffProcessId;

    /**
     * 员工id
     */
    @ApiModelProperty(name="staffId",value = "员工id")
    @Excel(name = "员工id", height = 20, width = 30)
    @Column(name = "staff_id")
    @NotNull(message = "员工id不能为空")
    private Long staffId;

    /**
     * 工种id
     */
    @ApiModelProperty(name="processId",value = "工种id")
    @Excel(name = "工种id", height = 20, width = 30)
    @Column(name = "process_id")
    private Long processId;

    /**
     * 有效开始时间
     */
    @ApiModelProperty(name="effectiveStartTime",value = "有效开始时间")
    @Excel(name = "有效开始时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "effective_start_time")
    private Date effectiveStartTime;

    /**
     * 有效结束时间
     */
    @ApiModelProperty(name="effectiveEndTime",value = "有效结束时间")
    @Excel(name = "有效结束时间", height = 20, width = 30,exportFormat ="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "effective_end_time")
    private Date effectiveEndTime;

    /**
     * 状态（0、不启用 1、启用）
     */
    @ApiModelProperty(name="status",value = "状态（0、不启用 1、启用）")
    @Excel(name = "状态（0、不启用 1、启用）", height = 20, width = 30)
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