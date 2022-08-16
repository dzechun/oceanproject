package com.fantechs.common.base.general.entity.qms;

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
 * IPQC检验单审核人表
 * qms_ipqc_inspection_order_audit_user
 * @author admin
 * @date 2021-06-08 11:05:05
 */
@Data
@Table(name = "qms_ipqc_inspection_order_audit_user")
public class QmsIpqcInspectionOrderAuditUser extends ValidGroup implements Serializable {
    /**
     * IPQC检验单审核人表ID
     */
    @ApiModelProperty(name="inspectionOrderAuditUserId",value = "IPQC检验单审核人表ID")
    @Excel(name = "IPQC检验单审核人表ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "inspection_order_audit_user_id")
    private Long inspectionOrderAuditUserId;

    /**
     * IPQC检验单ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "IPQC检验单ID")
    @Excel(name = "IPQC检验单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_id")
    private Long ipqcInspectionOrderId;

    /**
     * 审批人ID
     */
    @ApiModelProperty(name="auditUserId",value = "审批人ID")
    @Excel(name = "审批人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审批结果(0-NG 1-OK)
     */
    @ApiModelProperty(name="auditResult",value = "审批结果(0-NG 1-OK)")
    @Excel(name = "审批结果(0-NG 1-OK)", height = 20, width = 30,orderNum="") 
    @Column(name = "audit_result")
    private Byte auditResult;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    @Excel(name = "状态(0无效，1有效)", height = 20, width = 30,orderNum="") 
    private Byte status;

    /**
     * 附件路径
     */
    @ApiModelProperty(name="attachmentPath",value = "附件路径")
    @Excel(name = "附件路径", height = 20, width = 30,orderNum="") 
    @Column(name = "attachment_path")
    private String attachmentPath;

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
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Excel(name = "逻辑删除（0、删除 1、正常）", height = 20, width = 30,orderNum="") 
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="15")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="17")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 审批人名称
     */
    @Transient
    @ApiModelProperty(name = "auditUserName",value = "审批人名称")
    @Excel(name = "审批人名称", height = 20, width = 30,orderNum="2")
    private String auditUserName;

    /**
     * 审批人部门
     */
    @Column(name = "audit_dept_name")
    @ApiModelProperty(name = "auditDeptName",value = "审批人部门")
    @Excel(name = "审批人部门", height = 20, width = 30,orderNum="2")
    private String auditDeptName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}