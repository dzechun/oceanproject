package com.fantechs.common.base.general.entity.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 作业指导书-问题
 * eam_issue
 * @author admin
 * @date 2021-07-07 13:57:53
 */
@Data
@Table(name = "eam_issue")
public class EamIssue extends ValidGroup implements Serializable {
    /**
     * 问题ID
     */
    @ApiModelProperty(name="issueId",value = "问题ID")
    @Id
    @Column(name = "issue_id")
    @NotNull(groups = update.class,message = "问题ID不能为空")
    private Long issueId;

    /**
     * 问题编码
     */
    @ApiModelProperty(name="issueCode",value = "问题编码")
    @Excel(name = "问题编码", height = 20, width = 30,orderNum="1")
    @Column(name = "issue_code")
    private String issueCode;

    /**
     * 问题名称
     */
    @ApiModelProperty(name="issueName",value = "问题名称")
    @Excel(name = "问题名称", height = 20, width = 30,orderNum="2")
    @Column(name = "issue_name")
    @NotBlank(message = "问题名称不能为空")
    private String issueName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="7")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 问题附件
     */
    @ApiModelProperty(name="list",value = "问题附件")
    private List<EamIssueAttachment> list = new ArrayList<>();

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}