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
import java.util.List;

;

/**
 * 送货通知表
 * @date 2020-12-29 13:46:57
 */
@Data
@Table(name = "srm_delivery_note")
public class SrmDeliveryNote extends ValidGroup implements Serializable {
    /**
     * 送货通知ID
     */
    @ApiModelProperty(name="deliveryNoteId",value = "送货通知ID")
    @Excel(name = "送货通知ID", height = 20, width = 30,orderNum="")
    @Id
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

    /**
     * 送货通知单明细集合
     */
    private List<SrmDeliveryNoteDet> srmDeliveryNoteDets;

    private static final long serialVersionUID = 1L;
}
