package com.fantechs.common.base.general.entity.srm;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单生产情况表
 * srm_po_production_info
 * @author jbb
 * @date 2021-11-17 16:49:39
 */
@Data
@Table(name = "srm_po_production_info")
public class SrmPoProductionInfo extends ValidGroup implements Serializable {
    /**
     * 订单生产情况ID
     */
    @ApiModelProperty(name="poProductionInfoId",value = "订单生产情况ID")
    @Id
    @Column(name = "po_production_info_id")
    private Long poProductionInfoId;

    /**
     * 采购订单ID
     */
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    /**
     * 工单号
     */
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="2")
    @Column(name = "work_order_code")
    private String workOrderCode;

    /**
     * 供应商ID
     */
    @ApiModelProperty(name="supplierId",value = "供应商ID")
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Column(name = "material_id")
    private Long materialId;

    /**
     * 工单数量
     */
    @ApiModelProperty(name="workOrderQty",value = "工单数量")
    @Excel(name = "工单数量", height = 20, width = 30,orderNum="8")
    @Column(name = "work_order_qty")
    private BigDecimal workOrderQty;

    /**
     * 订单数量
     */
    @Transient
    @ApiModelProperty(name="orderQty",value = "订单数量")
    private BigDecimal orderQty;

    /**
     * 完工数量
     */
    @ApiModelProperty(name="outputQty",value = "完工数量")
    @Excel(name = "完工数量", height = 20, width = 30,orderNum="9")
    @Column(name = "output_qty")
    private BigDecimal outputQty;

    /**
     * 工单状态(1-待生产、2-生产中、3-完工)
     */
    @ApiModelProperty(name="workOrderStatus",value = "工单状态(1-待生产、2-生产中、3-完工)")
    @Excel(name = "工单状态", height = 20, width = 30,orderNum="10",replace = {"待生产_1","生产中_2","完工_3"})
    @Column(name = "work_order_status")
    private Byte workOrderStatus;

    /**
     * 实际开始时间
     */
    @ApiModelProperty(name="actualStartTime",value = "实际开始时间")
    @Excel(name = "实际开始时间", height = 20, width = 30,orderNum="11",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_start_time")
    private Date actualStartTime;

    /**
     * 实际结束时间
     */
    @ApiModelProperty(name="actualEndTime",value = "实际结束时间")
    @Excel(name = "实际结束时间", height = 20, width = 30,orderNum="12",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_end_time")
    private Date actualEndTime;

    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
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
    @Excel(name = "创建时间", height = 20, width = 30,orderNum="14",exportFormat ="yyyy-MM-dd HH:mm:ss")
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
    @Excel(name = "修改时间", height = 20, width = 30,orderNum="16",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * 逻辑删除（0、删除 1、正常）
     */
    @ApiModelProperty(name="isDelete",value = "逻辑删除（0、删除 1、正常）")
    @Column(name = "is_delete")
    private Byte isDelete;

    private String option1;

    private String option2;

    private String option3;

    private static final long serialVersionUID = 1L;
}
