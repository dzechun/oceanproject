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
 * 出货通知单
 * wms_out_shipping_note
 * @author hyc
 * @date 2021-01-09 15:10:02
 */
@Data
@Table(name = "wms_out_shipping_note")
public class WmsOutShippingNote extends ValidGroup implements Serializable {
    /**
     * 出货通知单ID
     */
    @ApiModelProperty(name="shippingNoteId",value = "出货通知单ID")
    @Id
    @Column(name = "shipping_note_id")
    @NotNull(groups = update.class,message = "出货通知单ID不能为空")
    private Long shippingNoteId;

    /**
     * 出货通知单号
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单号")
    @Excel(name = "出货通知单号", height = 20, width = 30,orderNum="2")
    @Column(name = "shipping_note_code")
    private String shippingNoteCode;

    /**
     * 订单合同号
     */
    @ApiModelProperty(name="orderContractCode",value = "订单合同号")
    @Excel(name = "订单合同号", height = 20, width = 30,orderNum="3")
    @Column(name = "order_contract_code")
    private String orderContractCode;

    /**
     * 消费国
     */
    @ApiModelProperty(name="country",value = "消费国")
    @Excel(name = "消费国", height = 20, width = 30,orderNum="6")
    private String country;

    /**
     * 运输方式（0-陆运 1-海运 2-空运）
     */
    @ApiModelProperty(name="trafficType",value = "运输方式（0-陆运 1-海运 2-空运）")
    @Excel(name = "运输方式（0-陆运 1-海运 2-空运）", height = 20, width = 30,orderNum="7",replace = {"陆运_0","海运_1","空运_2"})
    @Column(name = "traffic_type")
    private Byte trafficType;

    /**
     * 货币类型（0-人名币 1-美元 2-欧元）
     */
    @ApiModelProperty(name="currencyType",value = "货币类型（0-人名币 1-美元 2-欧元）")
    @Excel(name = "货币类型（0-人名币 1-美元 2-欧元）", height = 20, width = 30,orderNum="8",replace = {"人名币_0","美元_1","欧元_2"})
    @Column(name = "currency_type")
    private Byte currencyType;

    /**
     * 操作人ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作人ID")
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 处理人ID
     */
    @ApiModelProperty(name="processorUserId",value = "处理人ID")
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 备料状态（0-待备料 1-备料中 2- 备料完成）
     */
    @ApiModelProperty(name="stockStatus",value = "备料状态（0-待备料 1-备料中 2- 备料完成）")
    @Excel(name = "备料状态（0-待备料 1-备料中 2- 备料完成）", height = 20, width = 30,orderNum="9",replace = {"待备料_0","备料中_1","备料完成_2"})
    @Column(name = "stock_status")
    private Byte stockStatus;

    /**
     * 出库状态（0-待出库 1-出库中 2- 出库完成）
     */
    @ApiModelProperty(name="outStatus",value = "出库状态（0-待出库 1-出库中 2- 出库完成）")
    @Excel(name = "出库状态（0-待出库 1-出库中 2- 出库完成）", height = 20, width = 30,orderNum="10",replace = {"待出库_0","出库料中_1","出库完成_2"})
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="orderTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="11")
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 出货日期
     */
    @ApiModelProperty(name="outTime",value = "出货日期")
    @Excel(name = "出货日期", height = 20, width = 30,orderNum="12")
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 开船日期
     */
    @ApiModelProperty(name="sailTime",value = "开船日期")
    @Excel(name = "开船日期", height = 20, width = 30,orderNum="13")
    @Column(name = "sail_time")
    private Date sailTime;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    private String remark;

    /**
     * 组织id
     */
    @ApiModelProperty(name="organizationId",value = "组织id")
    @Column(name = "org_id")
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

    @ApiModelProperty(name="wmsOutShippingNoteDetList",value = "出货通知明细集合")
    private List<WmsOutShippingNoteDet> wmsOutShippingNoteDetList;

    private static final long serialVersionUID = 1L;
}