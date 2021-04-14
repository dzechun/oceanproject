package com.fantechs.common.base.general.entity.wms.out.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;

/**
 * 出货通知单
 * wms_out_ht_shipping_note
 * @author hyc
 * @date 2021-01-09 17:15:46
 */
@Data
@Table(name = "wms_out_ht_shipping_note")
public class WmsOutHtShippingNote implements Serializable {
    /**
     * 出货通知单履历ID
     */
    @ApiModelProperty(name="htShippingNoteId",value = "出货通知单履历ID")
    @Id
    @Column(name = "ht_shipping_note_id")
    private Long htShippingNoteId;

    /**
     * 出货通知单ID
     */
    @ApiModelProperty(name="shippingNoteId",value = "出货通知单ID")
    @Excel(name = "出货通知单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "shipping_note_id")
    private Long shippingNoteId;

    /**
     * 出货通知单号
     */
    @ApiModelProperty(name="shippingNoteCode",value = "出货通知单号")
    @Excel(name = "出货通知单号", height = 20, width = 30,orderNum="") 
    @Column(name = "shipping_note_code")
    private String shippingNoteCode;

    /**
     * 订单合同号
     */
    @ApiModelProperty(name="orderContractCode",value = "订单合同号")
    @Excel(name = "订单合同号", height = 20, width = 30,orderNum="") 
    @Column(name = "order_contract_code")
    private String orderContractCode;

    /**
     * 消费国
     */
    @ApiModelProperty(name="country",value = "消费国")
    @Excel(name = "消费国", height = 20, width = 30,orderNum="") 
    private String country;

    /**
     * 运输方式（0-陆运 1-海运 2-空运）
     */
    @ApiModelProperty(name="trafficType",value = "运输方式（0-陆运 1-海运 2-空运）")
    @Excel(name = "运输方式（0-陆运 1-海运 2-空运）", height = 20, width = 30,orderNum="") 
    @Column(name = "traffic_type")
    private Byte trafficType;

    /**
     * 货币类型（0-人名币 1-美元 2-欧元）
     */
    @ApiModelProperty(name="currencyType",value = "货币类型（0-人名币 1-美元 2-欧元）")
    @Excel(name = "货币类型（0-人名币 1-美元 2-欧元）", height = 20, width = 30,orderNum="") 
    @Column(name = "currency_type")
    private Byte currencyType;

    /**
     * 操作人ID
     */
    @ApiModelProperty(name="operatorUserId",value = "操作人ID")
    @Excel(name = "操作人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "operator_user_id")
    private Long operatorUserId;

    /**
     * 处理人ID
     */
    @ApiModelProperty(name="processorUserId",value = "处理人ID")
    @Excel(name = "处理人ID", height = 20, width = 30,orderNum="") 
    @Column(name = "processor_user_id")
    private Long processorUserId;

    /**
     * 单据状态（0-待备料 1-备料中 2- 备料完成）
     */
    @ApiModelProperty(name="outStatus",value = "单据状态（0-待备料 1-备料中 2- 备料完成）")
    @Excel(name = "单据状态（0-待备料 1-备料中 2- 备料完成）", height = 20, width = 30,orderNum="") 
    @Column(name = "out_status")
    private Byte outStatus;

    /**
     * 单据日期
     */
    @ApiModelProperty(name="orderTime",value = "单据日期")
    @Excel(name = "单据日期", height = 20, width = 30,orderNum="") 
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 出货日期
     */
    @ApiModelProperty(name="outTime",value = "出货日期")
    @Excel(name = "出货日期", height = 20, width = 30,orderNum="") 
    @Column(name = "out_time")
    private Date outTime;

    /**
     * 开船日期
     */
    @ApiModelProperty(name="sailTime",value = "开船日期")
    @Excel(name = "开船日期", height = 20, width = 30,orderNum="") 
    @Column(name = "sail_time")
    private Date sailTime;

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
    @Column(name = "org_id")
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

    /**
     * 操作人
     */
    @ApiModelProperty(name="operatorUserName",value = "操作人")
    @Excel(name = "操作人", height = 20, width = 30,orderNum="")
    private String operatorUserName;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserName",value = "处理人")
    @Excel(name = "处理人", height = 20, width = 30,orderNum="")
    private String processorUserName;

    /**
     * 组织代码
     */
    @ApiModelProperty(name="organizationCode",value = "组织代码")
    @Excel(name = "组织代码", height = 20, width = 30,orderNum="")
    private String organizationCode;

    private static final long serialVersionUID = 1L;
}