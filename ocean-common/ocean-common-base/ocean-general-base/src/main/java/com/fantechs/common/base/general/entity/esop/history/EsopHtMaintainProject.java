package com.fantechs.common.base.general.entity.esop.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 保养项目履历表
 * esop_ht_maintain_project
 * @author admin
 * @date 2021-06-25 15:33:12
 */
@Data
@Table(name = "esop_ht_maintain_project")
public class EsopHtMaintainProject extends ValidGroup implements Serializable {
    /**
     * 保养项目履历ID
     */
    @ApiModelProperty(name="htMaintainProjectId",value = "保养项目履历ID")
    @Excel(name = "保养项目履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_maintain_project_id")
    private Long htMaintainProjectId;

    /**
     * 保养项目ID
     */
    @ApiModelProperty(name="maintainProjectId",value = "保养项目ID")
    @Excel(name = "保养项目ID", height = 20, width = 30,orderNum="") 
    @Column(name = "maintain_project_id")
    private Long maintainProjectId;

    /**
     * 保养编码
     */
    @ApiModelProperty(name="maintainProjectCode",value = "保养编码")
    @Excel(name = "保养编码", height = 20, width = 30,orderNum="") 
    @Column(name = "maintain_project_code")
    private String maintainProjectCode;

    /**
     * 保养名称
     */
    @ApiModelProperty(name="maintainProjectName",value = "保养名称")
    @Excel(name = "保养名称", height = 20, width = 30,orderNum="") 
    @Column(name = "maintain_project_name")
    private String maintainProjectName;

    /**
     * 保养内容
     */
    @ApiModelProperty(name="maintainProjectContent",value = "保养内容")
    @Excel(name = "保养内容", height = 20, width = 30,orderNum="") 
    @Column(name = "maintain_project_content")
    private String maintainProjectContent;

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
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="4")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="6")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}