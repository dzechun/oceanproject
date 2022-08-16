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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;
;

/**
 * 检验单
 * qms_inspection_order
 * @author admin
 * @date 2021-05-25 10:24:13
 */
@Data
@Table(name = "qms_inspection_order")
public class QmsInspectionOrder extends ValidGroup implements Serializable {
    /**
     * 检验单ID
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单ID")
    @Id
    @Column(name = "inspection_order_id")
    private Long inspectionOrderId;

    /**
     * 检验单号
     */
    @ApiModelProperty(name="inspectionOrderCode",value = "检验单号")
    @Excel(name = "检验单号", height = 20, width = 30,orderNum="1")
    @Column(name = "inspection_order_code")
    private String inspectionOrderCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="customerId",value = "客户ID")
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 单据数量
     */
    @ApiModelProperty(name="orderQty",value = "单据数量")
    @Excel(name = "单据数量", height = 20, width = 30,orderNum="6")
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Column(name = "inspection_standard_id")
    private Long inspectionStandardId;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    @Excel(name = "检验状态(1-待检验 2-检验中 3-已检验)", height = 20, width = 30,orderNum="9")
    @Column(name = "inspection_status")
    private Byte inspectionStatus;

    /**
     * 检验结果(0-整批不合格 1-整批合格 2-部分不合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-整批不合格 1-整批合格 2-部分不合格)")
    @Excel(name = "检验结果(0-整批不合格 1-整批合格 2-部分不合格)", height = 20, width = 30,orderNum="10")
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 审核状态(0-未审核 1-通过 2-不通过)
     */
    @ApiModelProperty(name="auditStatus",value = "审核状态(0-未审核 1-通过 2-不通过)")
    @Column(name = "audit_status")
    private Byte auditStatus;

    /**
     * 审核部门ID
     */
    @ApiModelProperty(name="auditDeptId",value = "审核部门ID")
    @Column(name = "audit_dept_id")
    private Long auditDeptId;

    /**
     * 审核用户ID
     */
    @ApiModelProperty(name="auditUserId",value = "审核用户ID")
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核备注
     */
    @ApiModelProperty(name="auditRemark",value = "审核备注")
    @Column(name = "audit_remark")
    private String auditRemark;

    /**
     * 第三方检验(0-否 1-是)
     */
    @ApiModelProperty(name="ifThirdInspection",value = "第三方检验(0-否 1-是)")
    @Column(name = "if_third_inspection")
    private Byte ifThirdInspection;

    /**
     * 访问路径
     */
    @ApiModelProperty(name="accessUrl",value = "访问路径")
    @Column(name = "access_url")
    private String accessUrl;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
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
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="2")
    private String materialCode;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="3")
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="4")
    private String materialVersion;

    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "customerName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="5")
    private String customerName;

    /**
     * 检验标准
     */
    @Transient
    @ApiModelProperty(name = "inspectionStandardName",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="7")
    private String inspectionStandardName;

    /**
     * 检验方式
     */
    @Transient
    @ApiModelProperty(name = "inspectionWayCode",value = "检验方式")
    @Excel(name = "检验方式", height = 20, width = 30,orderNum="8")
    private String inspectionWayCode;

    /**
     * 审核部门
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "审核部门")
    private String deptName;

    /**
     * 审核人
     */
    @Transient
    @ApiModelProperty(name = "auditUserName",value = "审核人")
    private String auditUserName;

    /**
     * 成品检验单明细
     */
    @Transient
    @ApiModelProperty(name="qmsInspectionOrderDets",value = "成品检验单明细")
    private List<QmsInspectionOrderDet> qmsInspectionOrderDets = new ArrayList<>();

    /**
     * 库存ids
     */
    @Transient
    @ApiModelProperty(name = "inventoryIds",value = "库存ids")
    private String inventoryIds;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}