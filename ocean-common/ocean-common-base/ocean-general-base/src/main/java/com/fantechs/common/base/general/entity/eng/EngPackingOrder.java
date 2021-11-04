package com.fantechs.common.base.general.entity.eng;

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
import java.util.Date;

;
;

/**
 * 装箱单
 * eng_packing_order
 * @author 81947
 * @date 2021-08-27 09:05:44
 */
@Data
@Table(name = "eng_packing_order")
public class EngPackingOrder extends ValidGroup implements Serializable {
    /**
     * 装箱单ID
     */
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    @Id
    @Column(name = "packing_order_id")
    private Long packingOrderId;

    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    @Excel(name = "装箱单号", height = 20, width = 30,orderNum="2")
    @Column(name = "packing_order_code")
    private String packingOrderCode;

    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="1")
    @Column(name = "despatch_batch")
    private String despatchBatch;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderTime",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="3")
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 总箱数
     */
    @ApiModelProperty(name="totalCartonQty",value = "总箱数")
    @Excel(name = "总箱数", height = 20, width = 30,orderNum="4")
    @Column(name = "total_carton_qty")
    private Integer totalCartonQty;

    /**
     * 出厂时间
     */
    @ApiModelProperty(name="leaveFactoryTime",value = "出厂时间")
    @Excel(name = "出厂时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "leave_factory_time")
    private Date leaveFactoryTime;

    /**
     * 离港时间
     */
    @ApiModelProperty(name="leavePortTime",value = "离港时间")
    @Excel(name = "离港时间", height = 20, width = 30,orderNum="7",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "leave_port_time")
    private Date leavePortTime;

    /**
     * 到港时间
     */
    @ApiModelProperty(name="arrivalPortTime",value = "到港时间")
    @Excel(name = "到港时间", height = 20, width = 30,orderNum="8",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "arrival_port_time")
    private Date arrivalPortTime;

    /**
     * 到场时间
     */
    @ApiModelProperty(name="arrivalTime",value = "到场时间")
    @Excel(name = "到场时间", height = 20, width = 30,orderNum="9",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "arrival_time")
    private Date arrivalTime;

    /**
     * 订单状态(1-未到达 2-待收货 3-收货中 4-待上架 5-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-未到达 2-待收货 3-收货中 4-待上架 5-完成)")
    @Excel(name = "订单状态(1-未到达 2-待收货 3-收货中 4-待上架 5-完成)", height = 20, width = 30,orderNum="10")
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 审核状态(1-未审核 2-审核中 3-已通过 4-未通过)
     */
    @ApiModelProperty(name="auditStatus",value = "审核状态(1-未审核 2-审核中 3-已通过 4-未通过)")
    @Excel(name = "审核状态(1-未审核 2-审核中 3-已通过 4-未通过)", height = 20, width = 30)
    @Column(name = "audit_status")
    private Byte auditStatus;

    /**
     * 物流商ID
     */
    @ApiModelProperty(name="shipmentEnterpriseId",value = "物流商ID")
    @Column(name = "shipment_enterprise_id")
    private Long shipmentEnterpriseId;


    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="11")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="13",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="15",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    private String option1;

    private String option2;

    private String option3;

    /**
     * 到达登记人id
     */
    @ApiModelProperty(name = "agoConfirmUserId",value = "到达登记人id")
    @Column(name = "ago_confirm_user_id")
    private Long agoConfirmUserId;

    /**
     * 登记类型（1-出厂 2-离港 3-到港）
     */
    @Transient
    @ApiModelProperty(name="registerType",value = "登记类型（1-出厂 2-离港 3-到港）")
    private Byte registerType;

    private static final long serialVersionUID = 1L;
}