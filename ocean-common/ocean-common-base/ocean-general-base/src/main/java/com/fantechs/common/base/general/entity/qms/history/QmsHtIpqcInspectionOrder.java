package com.fantechs.common.base.general.entity.qms.history;

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
import java.math.BigDecimal;
import java.util.Date;

;
;

/**
 * IPQC检验单履历表
 * qms_ht_ipqc_inspection_order
 * @author admin
 * @date 2021-06-02 14:03:33
 */
@Data
@Table(name = "qms_ht_ipqc_inspection_order")
public class QmsHtIpqcInspectionOrder extends ValidGroup implements Serializable {
    /**
     * IPQC检验单履历ID
     */
    @ApiModelProperty(name="htIpqcInspectionOrderId",value = "IPQC检验单履历ID")
    @Excel(name = "IPQC检验单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_ipqc_inspection_order_id")
    private Long htIpqcInspectionOrderId;

    /**
     * IPQC检验单ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "IPQC检验单ID")
    @Excel(name = "IPQC检验单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_id")
    private Long ipqcInspectionOrderId;

    /**
     * IPQC检验单编码
     */
    @ApiModelProperty(name="ipqcInspectionOrderCode",value = "IPQC检验单编码")
    @Excel(name = "IPQC检验单编码", height = 20, width = 30,orderNum="") 
    @Column(name = "ipqc_inspection_order_code")
    private String ipqcInspectionOrderCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId",value = "工单ID")
    @Excel(name = "工单ID", height = 20, width = 30,orderNum="")
    @Column(name = "work_order_id")
    private Long workOrderId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 工序ID
     */
    @ApiModelProperty(name="processId",value = "工序ID")
    @Column(name = "process_id")
    private Long processId;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="")
    private BigDecimal qty;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Excel(name = "检验方式ID", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Excel(name = "检验标准ID", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_standard_id")
    private Long inspectionStandardId;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    @Excel(name = "检验状态(1-待检验 2-检验中 3-已检验)", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_status")
    private Byte inspectionStatus;

    /**
     * 检验结果(1-合格 2-不合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(1-合格 2-不合格)")
    @Excel(name = "检验结果(1-合格 2-不合格)", height = 20, width = 30,orderNum="")
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 审批状态(1-待审批 2-已审批)
     */
    @ApiModelProperty(name="auditStatus",value = "审批状态(1-待审批 2-已审批)")
    @Excel(name = "审批状态(1-待审批 2-已审批)", height = 20, width = 30,orderNum="")
    @Column(name = "audit_status")
    private Byte auditStatus;

    /**
     * 审批部门
     */
    @ApiModelProperty(name="auditDeptId",value = "审批部门")
    @Excel(name = "审批部门", height = 20, width = 30,orderNum="")
    @Column(name = "audit_dept_id")
    private Long auditDeptId;

    /**
     * 审批人ID
     */
    @ApiModelProperty(name="auditUserId",value = "审批人ID")
    @Excel(name = "审批人ID", height = 20, width = 30,orderNum="")
    @Column(name = "audit_user_id")
    private Long auditUserId;

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
    @Excel(name = "附件路径", height = 20, width = 30,orderNum="15")
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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="11")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="13")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="13")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="13")
    private String materialCode;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="13")
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="13")
    private String materialVersion;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="13")
    private String productModelName;

    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="13")
    private String supplierName;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    private String processName;

    /**
     * 检验方式
     */
    @Transient
    @ApiModelProperty(name = "inspectionWayDesc",value = "检验方式")
    @Excel(name = "检验方式", height = 20, width = 30,orderNum="13")
    private String inspectionWayDesc;

    /**
     * 检验标准
     */
    @Transient
    @ApiModelProperty(name = "inspectionStandardName",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="13")
    private String inspectionStandardName;

    /**
     * 检验标准版本
     */
    @Transient
    @ApiModelProperty(name = "inspectionStandardVersion",value = "检验标准版本")
    @Excel(name = "检验标准版本", height = 20, width = 30,orderNum="11")
    private String inspectionStandardVersion;

    /**
     * 审核部门
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "审核部门")
    @Excel(name = "审核部门", height = 20, width = 30,orderNum="13")
    private String deptName;

    /**
     * 审核人
     */
    @Transient
    @ApiModelProperty(name = "auditUserName",value = "审核人")
    @Excel(name = "审核人", height = 20, width = 30,orderNum="13")
    private String auditUserName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}