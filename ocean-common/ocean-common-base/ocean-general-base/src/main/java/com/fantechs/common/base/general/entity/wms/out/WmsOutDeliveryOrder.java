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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
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
     * 供应商id
     */
    @ApiModelProperty(name="supplierId",value = "客户ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 客户id
     */
    @ApiModelProperty(name = "customerId",value = "客户ID")
    @Column(name = "customer_id")
    private Long customerId;

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
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    @Column(name = "storage_id")
    private Long storageId;

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
     * 销售单号
     */
    @ApiModelProperty(name="salesOrderNo",value = "销售单号")
    @Column(name = "sales_order_no")
    private String salesOrderNo;

    /**
     * 出库单号
     */
    @ApiModelProperty(name="outOrderCode",value = "出库单号")
    @Column(name = "out_order_code")
    private String outOrderCode;

    /**
     * 柜号
     */
    @ApiModelProperty(name="containerNumber",value = "柜号")
    @Column(name = "container_number")
    private String containerNumber;

    /**
     * 报关地点
     */
    @ApiModelProperty(name="declarationLocation ",value = "报关地点")
    @Column(name = "declaration_location")
    private String declarationLocation;

    /**
     * 起运港
     */
    @ApiModelProperty(name="portFrom",value = "起运港")
    @Column(name = "port_from")
    private String portFrom;

    /**
     * 业务员
     */
    @ApiModelProperty(name="salesName",value = "业务员")
    @Column(name = "sales_name")
    private String salesName;

    @Transient
    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    //柜号、报关地点、起运港、业务员（接口获取，sh.CONTAINER_NUMBER AS "柜号",
    // sh.DECLARATION_LOCATION AS "报关地点",
    //sh.PORT_FROM AS "起运港",sh.salesname AS "业务员"），
    //出货通知单表体的“销售编码”（接口获取，sld.product_code AS "生产编码"）。

    /**
     * 月台id
     */
    @ApiModelProperty(name = "platformId",value = "月台")
    @Column(name = "platform_id")
    private Long platformId;

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
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "order_date")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "订单日期不能为空")
    private Date orderDate;

    /**
     * 预计发运日期
     */
    @ApiModelProperty(name="planDespatchDate",value = "预计发运日期")
    @Excel(name = "预计发运日期", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_despatch_date")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    //@NotNull(message = "预计发运日期不能为空")
    private Date planDespatchDate;

    /**
     * 实际发运日期
     */
    @ApiModelProperty(name="actualDespatchDate",value = "实际发运日期")
    @Excel(name = "实际发运日期", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_despatch_date")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date actualDespatchDate;

    /**
     * 要求到达日期
     */
    @ApiModelProperty(name="demandArriveDate",value = "要求到达日期")
    @Excel(name = "要求到达日期", height = 20, width = 30,orderNum="10",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "demand_arrive_date")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    //@NotNull(message = "要求到达日期不能为空")
    private Date demandArriveDate;

    /**
     * 收货人名称
     */
    @ApiModelProperty(name="consignee",value = "收货人名称")
    @Excel(name = "收货人名称", height = 20, width = 30,orderNum="13")
    @Column(name = "consignee")
    private String consignee;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="14")
    @Column(name = "link_man_name")
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系人电话", height = 20, width = 30,orderNum="15")
    @Column(name = "link_man_phone")
    private String linkManPhone;

    /**
     * 传真号码
     */
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30,orderNum="16")
    @Column(name = "fax_number")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(name="emailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30,orderNum="17")
    @Column(name = "e_mail_address")
    private String emailAddress;

    /**
     * 详细地址
     */
    @ApiModelProperty(name="detailedAddress",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="18")
    @Column(name = "detailed_address")
    private String detailedAddress;

    /**
     * 描述
     */
    @ApiModelProperty(name="descr",value = "描述")
    @Column(name = "descr")
    private String descr;

    /**
     * 领料人用户ID
     */
    @ApiModelProperty(name="pickMaterialUserId",value = "领料人用户ID")
    @Column(name = "pick_material_user_id")
    private Long pickMaterialUserId;

    /**
     * 领料人名称
     */
    @ApiModelProperty(name="pickMaterialUserName",value = "领料人名称")
    @Column(name = "pick_material_user_name")
    private String pickMaterialUserName;

    /**
     * 审批用户ID
     */
    @ApiModelProperty(name="auditUserId",value = "审批用户ID")
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审批人名称
     */
    @ApiModelProperty(name="auditUserName",value = "审批人名称")
    @Column(name = "audit_user_name")
    private String auditUserName;

    /**
     * 审批时间
     */
    @ApiModelProperty(name="auditTime",value = "审批时间")
    @Column(name = "audit_time")
    private String auditTime;


    /**
     * 是否创建作业单
     */
    @ApiModelProperty(name = "是否创建作业单 1-是 0-否",value = "ifCreatedJobOrder")
    @Column(name = "if_created_job_order")
    private Byte ifCreatedJobOrder;

    /**
     * 单据状态(1-待拣货，2-拣货中，3-待发运、4-部分发运、5-发运完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待拣货，2-拣货中，3-待发运、4-部分发运、5-发运完成)")
    @Excel(name = "单据状态(1-待拣货，2-拣货中，3-待发运、4-部分发运、5-发运完成)", height = 20, width = 30,orderNum="19")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 审核状态(0-未审核 1-已审核)
     */
    @ApiModelProperty(name="auditStatus",value = "审核状态(0-未审核 1-已审核)")
    @Excel(name = "审核状态(0-未审核 1-已审核)", height = 20, width = 30,orderNum="19")
    @Column(name = "audit_status")
    private Byte auditStatus;

    /**
     * 是否有效（0、无效 1、有效）
     */
    @ApiModelProperty(name="status",value = "是否有效（0、无效 1、有效）")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="20")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="22",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="24",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(name="option1",value = "扩展字段1")
    private String option1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(name="option2",value = "扩展字段2")
    private String option2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(name="option3",value = "扩展字段3")
    private String option3;

    /**
     * 扩展字段4
     */
    @ApiModelProperty(name="option4",value = "扩展字段4")
    private String option4;

    /**
     * 扩展字段5
     */
    @ApiModelProperty(name="option5",value = "扩展字段5")
    private String option5;

    /**
     * 扩展字段6
     */
    @ApiModelProperty(name="option6",value = "扩展字段6")
    private String option6;

    /**
     * 扩展字段7
     */
    @ApiModelProperty(name="option7",value = "扩展字段7")
    private String option7;

    /**
     * 扩展字段8
     */
    @ApiModelProperty(name="option8",value = "扩展字段8")
    private String option8;

    /**
     * 扩展字段9
     */
    @ApiModelProperty(name="option9",value = "扩展字段9")
    private String option9;

    /**
     * 扩展字段10
     */
    @ApiModelProperty(name="option10",value = "扩展字段10")
    private String option10;

    /**
     * 扩展字段11
     */
    @ApiModelProperty(name="option11",value = "扩展字段11")
    private String option11;

    /**
     * 扩展字段12
     */
    @ApiModelProperty(name="option12",value = "扩展字段12")
    private String option12;

    /**
     * 扩展字段13
     */
    @ApiModelProperty(name="option13",value = "扩展字段13")
    private String option13;

    /**
     * 扩展字段14
     */
    @ApiModelProperty(name="option14",value = "扩展字段14")
    private String option14;

    /**
     * 扩展字段15
     */
    @ApiModelProperty(name="option15",value = "扩展字段15")
    private String option15;

    @ApiModelProperty(name="wmsOutDeliveryOrderDetList",value = "出库单明细")
    private List<WmsOutDeliveryOrderDetDto> wmsOutDeliveryOrderDetList = new ArrayList<>();
}