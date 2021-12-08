package com.fantechs.common.base.general.entity.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 送货计划表
 * srm_plan_delivery_order_det
 * @author jbb
 * @date 2021-11-29 09:28:52
 */
@Data
@Table(name = "srm_plan_delivery_order_det")
public class SrmPlanDeliveryOrderDet extends ValidGroup implements Serializable {
    /**
     * 送货计划单明细ID
     */
    @ApiModelProperty(name="planDeliveryOrderDetId",value = "送货计划单明细ID")
    @Id
    @Column(name = "plan_delivery_order_det_id")
    private Long planDeliveryOrderDetId;

    /**
     * 送货计划单ID
     */
    @ApiModelProperty(name="planDeliveryOrderId",value = "送货计划单ID")
    @Column(name = "plan_delivery_order_id")
    private Long planDeliveryOrderId;

    /**
     * 采购订单明细ID
     */
    @ApiModelProperty(name="purchaseOrderDetId",value = "采购订单明细ID")
    @Column(name = "purchase_order_det_id")
    private Long purchaseOrderDetId;

    /**
     * 计划交货日期
     */
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Excel(name = "计划交货日期", height = 20, width = 30,orderNum="4",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_delivery_date")
    private Date planDeliveryDate;

    /**
     * 计划交货数量
     */
    @ApiModelProperty(name="planDeliveryQty",value = "计划交货数量")
    @Excel(name = "计划交货数量", height = 20, width = 30,orderNum="14")
    @Column(name = "plan_delivery_qty")
    private BigDecimal planDeliveryQty;

    /**
     * 是否已经生成ASN(0-否 1-是)
     */
    @ApiModelProperty(name="ifCreateAsn",value = "是否已经生成ASN(0-否 1-是)")
    @Excel(name = "是否已经生成ASN", height = 20, width = 30,orderNum="15",replace = {"否_0","是_1"})
    @Column(name = "if_create_asn")
    private Byte ifCreateAsn;

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
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}
