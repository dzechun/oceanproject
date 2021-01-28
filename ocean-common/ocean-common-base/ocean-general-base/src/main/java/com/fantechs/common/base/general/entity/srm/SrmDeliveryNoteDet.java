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
 * 送货通知明细表
 * @date 2020-12-29 19:46:04
 */
@Data
@Table(name = "srm_delivery_note_det")
public class SrmDeliveryNoteDet extends ValidGroup implements Serializable {
    /**
     * 送货通知明细ID
     */
    @ApiModelProperty(name="deliveryNoteDetId",value = "送货通知明细ID")
    @Excel(name = "送货通知明细ID", height = 20, width = 30)
    @Id
    @Column(name = "delivery_note_det_id")
    private Long deliveryNoteDetId;

    /**
     * 送货通知ID
     */
    @ApiModelProperty(name="deliveryNoteId",value = "送货通知ID")
    @Excel(name = "送货通知ID", height = 20, width = 30)
    @Column(name = "delivery_note_id")
    private Long deliveryNoteId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30)
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQuantity",value = "订单数量")
    @Excel(name = "订单数量", height = 20, width = 30,orderNum="")
    @Column(name = "order_quantity")
    private BigDecimal orderQuantity;

    /**
     * 交付数量
     */
    @ApiModelProperty(name="practicalDeliveryQuantity",value = "交付数量")
    @Excel(name = "交付数量", height = 20, width = 30,orderNum="")
    @Column(name = "practical_delivery_quantity")
    private BigDecimal practicalDeliveryQuantity;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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
