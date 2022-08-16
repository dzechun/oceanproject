package com.fantechs.common.base.general.entity.wanbao.history;

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
 * 检验单履历表
 * qms_ht_inspection_order
 * @author admin
 * @date 2021-05-25 10:24:14
 */
@Data
@Table(name = "qms_ht_inspection_order")
public class QmsHtInspectionOrder extends ValidGroup implements Serializable {
    /**
     * 检验单履历ID
     */
    @ApiModelProperty(name="htInspectionOrderId",value = "检验单履历ID")
    @Excel(name = "检验单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_inspection_order_id")
    private Long htInspectionOrderId;

    /**
     * 检验单ID
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单ID")
    @Excel(name = "检验单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_order_id")
    private Long inspectionOrderId;

    /**
     * 检验单号
     */
    @ApiModelProperty(name="inspectionOrderCode",value = "检验单号")
    @Excel(name = "检验单号", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_order_code")
    private String inspectionOrderCode;

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
    @ApiModelProperty(name="customerId",value = "客户ID")
    @Excel(name = "客户ID", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 销售编码
     */
    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30,orderNum="1")
    @Column(name = "sales_code")
    private String salesCode;

    /**
     * PO号
     */
    @ApiModelProperty(name="samePackageCode",value = "PO号")
    @Excel(name = "PO号", height = 20, width = 30,orderNum="1")
    @Column(name = "same_package_code")
    private String samePackageCode;

    /**
     * 已入库数量
     */
    @ApiModelProperty(name="inventoryQty",value = "已入库数量")
    @Excel(name = "已入库数量", height = 20, width = 30,orderNum="6")
    @Column(name = "inventory_qty")
    private BigDecimal inventoryQty;

    /**
     * 单据数量
     */
    @ApiModelProperty(name="orderQty",value = "单据数量")
    @Excel(name = "单据数量", height = 20, width = 30,orderNum="") 
    @Column(name = "order_qty")
    private BigDecimal orderQty;

    /**
     * 检验标准ID
     */
    @ApiModelProperty(name="inspectionStandardId",value = "检验标准ID")
    @Excel(name = "检验标准ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_standard_id")
    private Long inspectionStandardId;

    /**
     * 检验方式ID
     */
    @ApiModelProperty(name="inspectionWayId",value = "检验方式ID")
    @Excel(name = "检验方式ID", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_way_id")
    private Long inspectionWayId;

    /**
     * 检验状态(1-待检验 2-检验中 3-已检验)
     */
    @ApiModelProperty(name="inspectionStatus",value = "检验状态(1-待检验 2-检验中 3-已检验)")
    @Excel(name = "检验状态(1-待检验 2-检验中 3-已检验)", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_status")
    private Byte inspectionStatus;

    /**
     * 检验结果(0-不合格 1-合格)
     */
    @ApiModelProperty(name="inspectionResult",value = "检验结果(0-不合格 1-合格)")
    @Excel(name = "检验结果(0-不合格 1-合格)", height = 20, width = 30,orderNum="") 
    @Column(name = "inspection_result")
    private Byte inspectionResult;

    /**
     * 检验用户id
     */
    @ApiModelProperty(name="inspectionUserId",value = "检验用户id")
    @Column(name = "inspection_user_id")
    private Long inspectionUserId;

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
     * 检验类型（1、免检 2、第三方检验）
     */
    @ApiModelProperty(name="inspectionType",value = "检验类型（1、免检 2、第三方检验）")
    @Column(name = "inspection_type")
    private Byte inspectionType;

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
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
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
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="1")
    private String materialCode;

    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="1")
    private String materialDesc;

    /**
     * 产品版本
     */
    @Transient
    @ApiModelProperty(name = "materialVersion",value = "产品版本")
    @Excel(name = "产品版本", height = 20, width = 30,orderNum="1")
    private String materialVersion;

    /**
     * 客户
     */
    @Transient
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户", height = 20, width = 30,orderNum="1")
    private String supplierName;

    /**
     * 检验标准
     */
    @Transient
    @ApiModelProperty(name = "inspectionStandardName",value = "检验标准")
    @Excel(name = "检验标准", height = 20, width = 30,orderNum="1")
    private String inspectionStandardName;

    /**
     * 检验方式
     */
    @Transient
    @ApiModelProperty(name = "inspectionWayCode",value = "检验方式")
    @Excel(name = "检验方式", height = 20, width = 30,orderNum="1")
    private String inspectionWayCode;

    /**
     * 审核部门
     */
    @Transient
    @ApiModelProperty(name = "deptName",value = "审核部门")
    private String deptName;

    /**
     * 检验人
     */
    @Transient
    @ApiModelProperty(name = "inspectionUserName",value = "检验人")
    @Excel(name = "检验人", height = 20, width = 30,orderNum="15")
    private String inspectionUserName;

    /**
     * 审核人
     */
    @Transient
    @ApiModelProperty(name = "auditUserName",value = "审核人")
    private String auditUserName;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}