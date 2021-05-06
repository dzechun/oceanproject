package com.fantechs.common.base.general.entity.wms.in;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;;
import com.fantechs.common.base.general.entity.wms.WmsInAsnOrderDet;
import com.fantechs.common.base.support.ValidGroup;;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Advanced Shipping Note预先发货清单
 * wms_in_asn_order
 * @author mr.lei
 * @date 2021-04-29 14:42:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wms_in_asn_order")
public class WmsInAsnOrder extends ValidGroup implements Serializable {
    /**
     * ASN单ID
     */
    @ApiModelProperty(name="asnOrderId",value = "ASN单ID")
    @Excel(name = "ASN单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "asn_order_id")
    private Long asnOrderId;

    /**
     * 来源单据ID
     */
    @ApiModelProperty(name="orderId",value = "来源单据ID")
    @Excel(name = "来源单据ID", height = 20, width = 30,orderNum="") 
    @Column(name = "source_order_id")
    private Long sourceOrderId;

    /**
     * 货主信息
     */
    @ApiModelProperty(name="materialOwnerId",value = "货主信息")
    @Excel(name = "货主信息", height = 20, width = 30,orderNum="") 
    @Column(name = "material_owner_id")
    private Long materialOwnerId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 单据类型ID
     */
    @ApiModelProperty(name="orderTypeId",value = "单据类型ID")
    @Excel(name = "单据类型ID", height = 20, width = 30,orderNum="") 
    @Column(name = "order_type_id")
    private Long orderTypeId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Excel(name = "库位ID", height = 20, width = 30,orderNum="") 
    @Column(name = "storage_id")
    private Long storageId;

    /**
     * ASN单号
     */
    @ApiModelProperty(name="asnCode",value = "ASN单号")
    @Excel(name = "ASN单号", height = 20, width = 30,orderNum="") 
    @Column(name = "asn_code")
    private String asnCode;

    /**
     * 客户单号
     */
    @ApiModelProperty(name="customerOrderCode",value = "客户单号")
    @Excel(name = "客户单号", height = 20, width = 30,orderNum="") 
    @Column(name = "customer_order_code")
    private String customerOrderCode;

    /**
     * 相关单号1
     */
    @ApiModelProperty(name="relatedOrderCode1",value = "相关单号1")
    @Excel(name = "相关单号1", height = 20, width = 30,orderNum="") 
    @Column(name = "related_order_code_1")
    private String relatedOrderCode1;

    /**
     * 相关单号2
     */
    @ApiModelProperty(name="relatedOrderCode2",value = "相关单号2")
    @Excel(name = "相关单号2", height = 20, width = 30,orderNum="") 
    @Column(name = "related_order_code_2")
    private String relatedOrderCode2;

    /**
     * 相关单号3
     */
    @ApiModelProperty(name="relatedOrderCode3",value = "相关单号3")
    @Excel(name = "相关单号3", height = 20, width = 30,orderNum="") 
    @Column(name = "related_order_code_3")
    private String relatedOrderCode3;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderDate",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="") 
    @Column(name = "order_date")
    private Date orderDate;

    /**
     * 预计到货(arrival of good)日期
     */
    @ApiModelProperty(name="planAgoDate",value = "预计到货(arrival of good)日期")
    @Excel(name = "预计到货(arrival of good)日期", height = 20, width = 30,orderNum="") 
    @Column(name = "plan_ago_date")
    private Date planAgoDate;

    /**
     * 开始收货日期
     */
    @ApiModelProperty(name="startReceivingDate",value = "开始收货日期")
    @Excel(name = "开始收货日期", height = 20, width = 30,orderNum="") 
    @Column(name = "start_receiving_date")
    private Date startReceivingDate;

    /**
     * 结束收货日期
     */
    @ApiModelProperty(name="endReceivingDate",value = "结束收货日期")
    @Excel(name = "结束收货日期", height = 20, width = 30,orderNum="") 
    @Column(name = "end_receiving_date")
    private Date endReceivingDate;

    /**
     * 发货人名称
     */
    @ApiModelProperty(name="shipperName",value = "发货人名称")
    @Excel(name = "发货人名称", height = 20, width = 30,orderNum="") 
    @Column(name = "shipper_name")
    private String shipperName;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="") 
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系人电话", height = 20, width = 30,orderNum="") 
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30,orderNum="") 
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30,orderNum="") 
    @Column(name = "e_mail_address")
    private String eMailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="detailedAddress",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="") 
    @Column(name = "detailed_address")
    private String detailedAddress;

    /**
     * 描述
     */
    @ApiModelProperty(name="descr",value = "描述")
    @Excel(name = "描述", height = 20, width = 30,orderNum="") 
    private String descr;

    /**
     * 单据状态(1-待收货 2-收货中 3-收货完成)
     */
    @ApiModelProperty(name = "orderStatus",value = "单据状态(1-待收货 2-收货中 3-收货完成)")
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

    @ApiModelProperty(name = "完工入库明细",value = "wmsInAsnOrderDetList")
    private List<WmsInAsnOrderDet> wmsInAsnOrderDetList;

    private static final long serialVersionUID = 1L;
}