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
import java.math.BigDecimal;
import java.util.Date;

;

/**
 * 送货通知历史表
 * srm_ht_delivery_note
 * @author jbb
 * @date 2020-12-29 19:47:58
 */
@Data
@Table(name = "srm_ht_delivery_note")
public class SrmHtDeliveryNote extends ValidGroup implements Serializable {
    /**
     * 送货通知历史ID
     */
    @ApiModelProperty(name="htDeliveryNoteId",value = "送货通知历史ID")
    @Excel(name = "送货通知历史ID", height = 20, width = 30,orderNum="")
    @Id
    @Column(name = "ht_delivery_note_id")
    private Long htDeliveryNoteId;

    /**
     * 送货通知ID
     */
    @ApiModelProperty(name="deliveryNoteId",value = "送货通知ID")
    @Excel(name = "送货通知ID", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_note_id")
    private Long deliveryNoteId;

    /**
     * ASN编码
     */
    @ApiModelProperty(name="asnCode",value = "ASN编码")
    @Excel(name = "ASN编码", height = 20, width = 30,orderNum="")
    @Column(name = "asn_code")
    private String asnCode;

    /**
     * 客户订单ID
     */
    @ApiModelProperty(name="orderId",value = "客户订单ID")
    @Excel(name = "客户订单ID", height = 20, width = 30,orderNum="")
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单类别（0、送货计划单 1、采购订单）
     */
    @ApiModelProperty(name="orderType",value = "订单类别（0、送货计划单 1、采购订单）")
    @Excel(name = "订单类别（0、送货计划单 1、采购订单）", height = 20, width = 30,orderNum="")
    @Column(name = "order_type")
    private Byte orderType;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="documentDate",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="")
    @Column(name = "document_date")
    private Date documentDate;

    /**
     * 交付日期
     */
    @ApiModelProperty(name="deliveryDate",value = "交付日期")
    @Excel(name = "交付日期", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_date")
    private Date deliveryDate;

    /**
     * 交付数量
     */
    @ApiModelProperty(name="deliveryQuantity",value = "交付数量")
    @Excel(name = "交付数量", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_quantity")
    private BigDecimal deliveryQuantity;

    /**
     * 送货人
     */
    @ApiModelProperty(name="deliveryMan",value = "送货人")
    @Excel(name = "送货人", height = 20, width = 30,orderNum="")
    @Column(name = "delivery_man")
    private Long deliveryMan;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
    private Long organizationId;

    /**
     * 组织名称
     */
    @ApiModelProperty(name="organizationName",value = "组织名称")
    @Transient
    private String organizationName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 状态（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    @Excel(name = "状态（0、无效 1、有效）", height = 20, width = 30,orderNum="")
    private Byte status;

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

    private static final long serialVersionUID = 1L;
}
