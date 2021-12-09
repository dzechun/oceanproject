package com.fantechs.common.base.general.entity.srm.history;

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
 * 送货计划履历表
 * srm_ht_plan_delivery_order
 * @author jbb
 * @date 2021-11-29 09:28:52
 */
@Data
@Table(name = "srm_ht_plan_delivery_order")
public class SrmHtPlanDeliveryOrder extends ValidGroup implements Serializable {
    /**
     * 送货计划单履历ID
     */
    @ApiModelProperty(name="htPlanDeliveryOrderId",value = "送货计划单履历ID")
    @Excel(name = "送货计划单履历ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_plan_delivery_order_id")
    private Long htPlanDeliveryOrderId;

    /**
     * 送货计划单ID
     */
    @ApiModelProperty(name="planDeliveryOrderId",value = "送货计划单ID")
    @Excel(name = "送货计划单ID", height = 20, width = 30,orderNum="")
    @Column(name = "plan_delivery_order_id")
    private Long planDeliveryOrderId;

    /**
     * 核心系统单据类型编码
     */
    @ApiModelProperty(name="corSourceSysOrderTypeCode",value = "核心系统单据类型编码")
    @Column(name = "core_source_sys_order_type_code")
    private String corSourceSysOrderTypeCode;

    /**
     * 来源系统单据类型编码
     */
    @ApiModelProperty(name="sourceSysOrderTypeCode",value = "来源系统单据类型编码")
    @Column(name = "source_sys_order_type_code")
    private String sourceSysOrderTypeCode;

    /**
     * 送货计划单编码
     */
    @ApiModelProperty(name="planDeliveryOrderCode",value = "送货计划单编码")
    @Excel(name = "送货计划单编码", height = 20, width = 30,orderNum="")
    @Column(name = "plan_delivery_order_code")
    private String planDeliveryOrderCode;

    /**
     * 系统单据类型编码
     */
    @ApiModelProperty(name="sysOrderTypeCode",value = "系统单据类型编码")
    @Excel(name = "系统单据类型编码", height = 20, width = 30,orderNum="0")
    @Column(name = "sys_order_type_code")
    private String sysOrderTypeCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 单据状态(1-未提交 2-提交)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-未提交 2-提交)")
    @Excel(name = "单据状态(1-未提交 2-提交)", height = 20, width = 30,orderNum="")
    @Column(name = "order_status")
    private Byte orderStatus;

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

    private String option1;

    private String option2;

    private String option3;

    /**
     * 供应商名称
     */
    @Transient
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;

    private static final long serialVersionUID = 1L;
}
