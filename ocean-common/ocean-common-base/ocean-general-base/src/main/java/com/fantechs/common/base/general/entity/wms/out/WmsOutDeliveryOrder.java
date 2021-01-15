package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;

/**
 * 出库单
 * wms_out_delivery_order
 * @author hyc
 * @date 2021-01-09 20:01:32
 */
@Data
@Table(name = "wms_out_delivery_order")
public class WmsOutDeliveryOrder extends ValidGroup implements Serializable {
    /**
     * 出库单ID
     */
    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    @Id
    @Column(name = "delivery_order_id")
    @NotNull(groups = update.class,message = "出库单ID不能为空")
    private Long deliveryOrderId;

    /**
     * 出库单号
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出库单号")
    @Excel(name = "出库单号", height = 20, width = 30,orderNum="1")
    @Column(name = "delivery_order_code")
    private String deliveryOrderCode;

    /**
     * 出货通知单
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单")
    @Excel(name = "出货通知单", height = 20, width = 30,orderNum="2")
    @Column(name = "shipping_note_code")
    private String shippingNoteCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="outTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="4")
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 单据状态（0-待出库 1-出库中 2-出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待出库 1-出库中 2-出库完成）")
    @Excel(name = "单据状态（0-待出库 1-出库中 2-出库完成）", height = 20, width = 30,orderNum="5")
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Column(name = "status")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "organization_id")
    private Long organizationId;

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

    @ApiModelProperty(name="wmsOutDeliveryOrderDetList",value = "出库单明细")
    private List<WmsOutDeliveryOrderDet> wmsOutDeliveryOrderDetList;

    private static final long serialVersionUID = 1L;
}