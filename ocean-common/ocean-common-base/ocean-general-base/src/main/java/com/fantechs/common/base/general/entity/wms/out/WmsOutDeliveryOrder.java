package com.fantechs.common.base.general.entity.wms.out;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

;
;

/**
 * 出库单
 * wms_out_delivery_order
 * @author admin
 * @date 2021-05-07 16:44:15
 */
@Data
@Table(name = "wms_out_delivery_order")
public class WmsOutDeliveryOrder extends ValidGroup implements Serializable {

    private static final long serialVersionUID = 5075413536828074017L;
    /**
     * 出库单ID
     */
    @ApiModelProperty(name="deliveryOrderId",value = "出库单ID")
    @Id
    @Column(name = "delivery_order_id")
    @NotNull(groups = update.class,message = "出库单ID不能为空")
    private Long deliveryOrderId;

    /**
     * 来源单据ID
     */
    @ApiModelProperty(name="sourceOrderId",value = "来源单据ID")
    @Column(name = "source_order_id")
    private Long sourceOrderId;

    /**
     * 货主信息ID
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息ID")
    @Column(name = "material_owner_id")
    @NotNull(message = "货主信息ID不能为空")
    private Long materialOwnerId;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 物流商ID
     */
    @ApiModelProperty(name="shipmentEnterpriseId",value = "物流商ID")
    @Column(name = "shipment_enterprise_id")
    private Long shipmentEnterpriseId;

    /**
     * 单据类型ID
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    @Column(name = "order_type_id")
    private Long orderTypeId;

    /**
     * 出库单编码
     */
    @ApiModelProperty(name="deliveryOrderCode",value = "出库单编码")
    @Excel(name = "出库单编码", height = 20, width = 30,orderNum="1")
    @Column(name = "delivery_order_code")
    //@NotBlank(message = "出库单编码不能为空")
    private String deliveryOrderCode;

    /**
     * 客户单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户单号")
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    /**
     * 相关单号1
     */
    @ApiModelProperty(name="relatedOrderCode1",value = "相关单号1")
    @Excel(name = "相关单号1", height = 20, width = 30,orderNum="2")
    @Column(name = "related_order_code_1")
    private String relatedOrderCode1;

    /**
     * 相关单号2
     */
    @ApiModelProperty(name="relatedOrderCode2",value = "相关单号2")
    //@Excel(name = "相关单号2", height = 20, width = 30,orderNum="")
    @Column(name = "related_order_code_2")
    private String relatedOrderCode2;

    /**
     * 相关单号3
     */
    @ApiModelProperty(name="relatedOrderCode3",value = "相关单号3")
    //@Excel(name = "相关单号3", height = 20, width = 30,orderNum="")
    @Column(name = "related_order_code_3")
    private String relatedOrderCode3;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="6")
    @Column(name = "order_date")
    @NotNull(message = "订单日期不能为空")
    private Date orderDate;

    /**
     * 预计发运日期
     */
    @ApiModelProperty(name="planDespatchDate",value = "预计发运日期")
    @Excel(name = "预计发运日期", height = 20, width = 30,orderNum="8")
    @Column(name = "plan_despatch_date")
    @NotNull(message = "预计发运日期不能为空")
    private Date planDespatchDate;

    /**
     * 实际发运日期
     */
    @ApiModelProperty(name="actualDespatchDate",value = "实际发运日期")
    @Excel(name = "实际发运日期", height = 20, width = 30,orderNum="9")
    @Column(name = "actual_despatch_date")
    private Date actualDespatchDate;

    /**
     * 要求到达日期
     */
    @ApiModelProperty(name="demandArriveDate",value = "要求到达日期")
    @Excel(name = "要求到达日期", height = 20, width = 30,orderNum="7")
    @Column(name = "demand_arrive_date")
    @NotNull(message = "要求到达日期不能为空")
    private Date demandArriveDate;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name="consignee",value = "收货人名称")
    @Excel(name = "收货人名称", height = 20, width = 30,orderNum="10")
    @Column(name = "consignee")
    private String consignee;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="11")
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系人电话", height = 20, width = 30,orderNum="12")
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30,orderNum="13")
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="emailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30,orderNum="14")
    @Column(name = "e_mail_address")
    private String emailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="detailedAddress",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="15")
    @Column(name = "detailed_address")
    private String detailedAddress;

    /**
     * 描述
     */
    @ApiModelProperty(name="descr",value = "描述")
    @Column(name = "descr")
    private String descr;

    /**
     * 单据状态(1-待发货 2-发货中 3-发货完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待发货 2-发货中 3-发货完成)")
    @Excel(name = "单据状态(1-待发货 2-发货中 3-发货完成)", height = 20, width = 30,orderNum="16")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="17")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="19",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="21",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    private List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList;
}