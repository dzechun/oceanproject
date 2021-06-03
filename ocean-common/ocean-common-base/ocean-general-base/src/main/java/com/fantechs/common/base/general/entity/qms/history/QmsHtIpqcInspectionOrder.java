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
     * 生产订单条码ID
     */
    @ApiModelProperty(name="workOrderBarcodeId",value = "生产订单条码ID")
    @Excel(name = "生产订单条码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "work_order_barcode_id")
    private Long workOrderBarcodeId;

    /**
     * 过程检验项目编码ID
     */
    @ApiModelProperty(name="processInspectionItemId",value = "过程检验项目编码ID")
    @Excel(name = "过程检验项目编码ID", height = 20, width = 30,orderNum="") 
    @Column(name = "process_inspection_item_id")
    private Long processInspectionItemId;

    /**
     * 部门ID
     */
    @ApiModelProperty(name="deptId",value = "部门ID")
    @Excel(name = "部门ID", height = 20, width = 30,orderNum="") 
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 审批状态(1-待审批 2-已审批)
     */
    @ApiModelProperty(name="approveStatus",value = "审批状态(1-待审批 2-已审批)")
    @Excel(name = "审批状态(1-待审批 2-已审批)", height = 20, width = 30,orderNum="") 
    @Column(name = "approve_status")
    private Byte approveStatus;

    /**
     * 审批人ID
     */
    @ApiModelProperty(name="approveUserId",value = "审批人ID")
    @Excel(name = "审批人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "approve_user_id")
    private Long approveUserId;

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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}