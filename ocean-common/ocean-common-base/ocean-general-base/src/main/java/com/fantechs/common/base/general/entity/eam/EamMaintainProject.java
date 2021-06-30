package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 保养项目
 * eam_maintain_project
 * @author admin
 * @date 2021-06-25 15:33:12
 */
@Data
@Table(name = "eam_maintain_project")
public class EamMaintainProject extends ValidGroup implements Serializable {
    /**
     * 保养项目ID
     */
    @ApiModelProperty(name="maintainProjectId",value = "保养项目ID")
    @Id
    @Column(name = "maintain_project_id")
    @NotNull(groups = update.class,message = "保养项目ID不能为空")
    private Long maintainProjectId;

    /**
     * 保养编码
     */
    @ApiModelProperty(name="maintainProjectCode",value = "保养编码")
    @Excel(name = "保养编码", height = 20, width = 30,orderNum="1")
    @Column(name = "maintain_project_code")
    private String maintainProjectCode;

    /**
     * 保养名称
     */
    @ApiModelProperty(name="maintainProjectName",value = "保养名称")
    @Excel(name = "保养名称", height = 20, width = 30,orderNum="2")
    @Column(name = "maintain_project_name")
    private String maintainProjectName;

    /**
     * 保养内容
     */
    @ApiModelProperty(name="maintainProjectContent",value = "保养内容")
    @Excel(name = "保养内容", height = 20, width = 30,orderNum="3")
    @Column(name = "maintain_project_content")
    private String maintainProjectContent;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="4")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}