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
 * 作业指导书-问题履历表
 * esop_ht_issue
 * @author admin
 * @date 2021-07-07 13:57:53
 */
@Data
@Table(name = "esop_ht_issue")
public class EsopHtIssue extends ValidGroup implements Serializable {
    /**
     * 问题履历ID
     */
    @ApiModelProperty(name="htIssueId",value = "问题履历ID")
    @Excel(name = "问题履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_issue_id")
    private Long htIssueId;

    /**
     * 问题ID
     */
    @ApiModelProperty(name="issueId",value = "问题ID")
    @Excel(name = "问题ID", height = 20, width = 30,orderNum="") 
    @Column(name = "issue_id")
    private Long issueId;

    /**
     * 问题编码
     */
    @ApiModelProperty(name="issueCode",value = "问题编码")
    @Excel(name = "问题编码", height = 20, width = 30,orderNum="") 
    @Column(name = "issue_code")
    private String issueCode;

    /**
     * 问题名称
     */
    @ApiModelProperty(name="issueName",value = "问题名称")
    @Excel(name = "问题名称", height = 20, width = 30,orderNum="") 
    @Column(name = "issue_name")
    private String issueName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="") 
    @Column(name = "material_id")
    private Long materialId;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="10")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="12")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 产品编码
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品编码")
    @Excel(name = "产品编码", height = 20, width = 30,orderNum="12")
    private String materialCode;

    /**
     * 产品名称
     */
    @Transient
    @ApiModelProperty(name = "materialName",value = "产品名称")
    @Excel(name = "产品名称", height = 20, width = 30,orderNum="12")
    private String materialName;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="12")
    private String productModelName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}