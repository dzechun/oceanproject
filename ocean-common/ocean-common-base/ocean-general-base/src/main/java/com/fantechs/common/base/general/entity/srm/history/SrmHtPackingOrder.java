package com.fantechs.common.base.general.entity.srm.history;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

;
;

/**
 * 装箱单履历表
 * eng_ht_packing_order
 * @author 81947
 * @date 2021-08-27 09:05:45
 */
@Data
@Table(name = "eng_ht_packing_order")
public class SrmHtPackingOrder extends ValidGroup implements Serializable {
    /**
     * 装箱单履历ID
     */
    @ApiModelProperty(name="htPackingOrderId",value = "装箱单履历ID")
    @Excel(name = "装箱单履历ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "ht_packing_order_id")
    private Long htPackingOrderId;

    /**
     * 装箱单ID
     */
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    @Excel(name = "装箱单ID", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_order_id")
    private Long packingOrderId;

    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    @Excel(name = "装箱单号", height = 20, width = 30,orderNum="") 
    @Column(name = "packing_order_code")
    private String packingOrderCode;

    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="") 
    @Column(name = "despatch_batch")
    private String despatchBatch;

    /**
     * 订单日期
     */
    @ApiModelProperty(name="orderTime",value = "订单日期")
    @Excel(name = "订单日期", height = 20, width = 30,orderNum="") 
    @Column(name = "order_time")
    private Date orderTime;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Excel(name = "供应商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 总箱数
     */
    @ApiModelProperty(name="totalCartonQty",value = "总箱数")
    @Excel(name = "总箱数", height = 20, width = 30,orderNum="") 
    @Column(name = "total_carton_qty")
    private Integer totalCartonQty;

    /**
     * 出厂时间
     */
    @ApiModelProperty(name="leaveFactoryTime",value = "出厂时间")
    @Excel(name = "出厂时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "leave_factory_time")
    private Date leaveFactoryTime;

    /**
     * 离港时间
     */
    @ApiModelProperty(name="leavePortTime",value = "离港时间")
    @Excel(name = "离港时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "leave_port_time")
    private Date leavePortTime;

    /**
     * 到港时间
     */
    @ApiModelProperty(name="arrivalPortTime",value = "到港时间")
    @Excel(name = "到港时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "arrival_port_time")
    private Date arrivalPortTime;

    /**
     * 到场时间
     */
    @ApiModelProperty(name="arrivalTime",value = "到场时间")
    @Excel(name = "到场时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "arrival_time")
    private Date arrivalTime;

    /**
     * 订单状态(1-未审核 2-审核中 3-已通过 4-未通过)
     */
    @ApiModelProperty(name="orderStatus",value = "订单状态(1-未审核 2-审核中 3-已通过 4-未通过)")
    @Excel(name = "订单状态(1-未审核 2-审核中 3-已通过 4-未通过)", height = 20, width = 30,orderNum="") 
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

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}