package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 作业指导书-工装设备(Facility & Tooling)及检具要求
 * eam_wi_f_t_and_inspection_tool
 * @author 81947
 * @date 2021-07-06 14:05:20
 */
@Data
@Table(name = "eam_wi_f_t_and_inspection_tool")
public class EamWiFTAndInspectionTool extends ValidGroup implements Serializable {
    /**
     * 工装设备及检具要求ID
     */
    @ApiModelProperty(name="wiFTAndInspectionToolId",value = "工装设备及检具要求ID")
    @Id
    @Column(name = "wi_f_t_and_inspection_tool_id")
    private Long wiFTAndInspectionToolId;

    /**
     * 作业指导书ID
     */
    @ApiModelProperty(name="workInstructionId",value = "作业指导书ID")
    @Column(name = "work_instruction_id")
    private Long workInstructionId;

    /**
     * 工具/设备
     */
    @ApiModelProperty(name="facilityTooling",value = "工具/设备")
    @Excel(name = "工具/设备", height = 20, width = 30,orderNum="1")
    @Column(name = "facility_tooling")
    private String facilityTooling;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="2")
    private String qty;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="3")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="orgId",value = "组织id")
    @Column(name = "org_id")
    private Long orgId;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}