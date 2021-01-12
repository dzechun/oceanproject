package com.fantechs.common.base.general.entity.wms.out.history;

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

;

/**
 * 出库单履历
 * wms_out_ht_delivery_order
 * @author hyc
 * @date 2021-01-09 20:01:33
 */
@Data
@Table(name = "wms_out_ht_delivery_order")
public class WmsOutHtDeliveryOrder extends ValidGroup implements Serializable {
    /**
     * 出库单履历ID
     */
    @ApiModelProperty(name="htDeliveryOrderId",value = "出库单履历ID")
    @NotNull(groups = update.class,message = "出库单履历ID不能为空")
    @Id
    @Column(name = "ht_delivery_order_id")
    private Long htDeliveryOrderId;

    /**
     * 出库单ID
     */
    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    @Excel(name = "出库单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_order_id")
    private Long deliveryOrderId;

    /**
     * 出库单编码
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出库单编码")
    @Excel(name = "出库单编码", height = 20, width = 30,orderNum="") 
    @Column(name = "delivery_order_code")
    private String deliveryOrderCode;

    /**
     * 出货通知单
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单")
    @Excel(name = "出货通知单", height = 20, width = 30,orderNum="") 
    @Column(name = "shipping_note_code")
    private String shippingNoteCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="") 
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="outTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="") 
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 单据状态（0-待出库 1-出库中 2-出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待出库 1-出库中 2-出库完成）")
    @Excel(name = "单据状态（0-待出库 1-出库中 2-出库完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    @Excel(name = "是否有效（0、无效 1、有效）", height = 20, width = 30,orderNum="") 
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
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Excel(name = "组织id", height = 20, width = 30,orderNum="") 
    @Column(name = "organization_id")
    private Long organizationId;

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