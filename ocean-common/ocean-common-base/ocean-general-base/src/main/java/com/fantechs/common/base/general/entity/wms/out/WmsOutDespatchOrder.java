package com.fantechs.common.base.general.entity.wms.out;

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
import java.util.List;

;
;

/**
 * 装车单
 * wms_out_despatch_order
 * @author mr.lei
 * @date 2021-05-10 10:40:02
 */
@Data
@Table(name = "wms_out_despatch_order")
public class WmsOutDespatchOrder extends ValidGroup implements Serializable {
    /**
     * 装车单ID
     */
    @ApiModelProperty(name="despatchOrderId",value = "装车单ID")
    @Excel(name = "装车单ID", height = 20, width = 30,orderNum="") 
    @Id
    @Column(name = "despatch_order_id")
    private Long despatchOrderId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    @Excel(name = "仓库ID", height = 20, width = 30,orderNum="") 
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 物流商ID
     */
    @ApiModelProperty(name="shipmentEnterpriseId",value = "物流商ID")
    @Excel(name = "物流商ID", height = 20, width = 30,orderNum="") 
    @Column(name = "shipment_enterprise_id")
    private Long shipmentEnterpriseId;

    /**
     * 装车单号
     */
    @ApiModelProperty(name="despatchOrderCode",value = "装车单号")
    @Excel(name = "装车单号", height = 20, width = 30,orderNum="") 
    @Column(name = "despatch_order_code")
    private String despatchOrderCode;

    /**
     * 车型
     */
    @ApiModelProperty(name="carType",value = "车型")
    @Excel(name = "车型", height = 20, width = 30,orderNum="") 
    @Column(name = "car_type")
    private String carType;

    /**
     * 车牌
     */
    @ApiModelProperty(name="carNumber",value = "车牌")
    @Excel(name = "车牌", height = 20, width = 30,orderNum="") 
    @Column(name = "car_number")
    private String carNumber;

    /**
     * 司机名字
     */
    @ApiModelProperty(name="driverName",value = "司机名字")
    @Excel(name = "司机名字", height = 20, width = 30,orderNum="") 
    @Column(name = "driver_name")
    private String driverName;

    /**
     * 柜号
     */
    @ApiModelProperty(name="containerNumber",value = "柜号")
    @Excel(name = "柜号", height = 20, width = 30,orderNum="") 
    @Column(name = "container_number")
    private String containerNumber;

    /**
     * 封条号
     */
    @ApiModelProperty(name="sealNumber",value = "封条号")
    @Excel(name = "封条号", height = 20, width = 30,orderNum="") 
    @Column(name = "seal_number")
    private String sealNumber;

    /**
     * 预计发车时间
     */
    @ApiModelProperty(name="planDespatchTime",value = "预计发车时间")
    @Excel(name = "预计发车时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_despatch_time")
    private Date planDespatchTime;

    /**
     * 实际发车时间
     */
    @ApiModelProperty(name="actualDespatchTime",value = "实际发车时间")
    @Excel(name = "实际发车时间", height = 20, width = 30,orderNum="",exportFormat ="yyyy-MM-dd HH:mm:ss") 
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_despatch_time")
    private Date actualDespatchTime;

    /**
     * 单据状态(1-待装车 2-装车中 3-完成)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待装车 2-装车中 3-完成)")
    @Excel(name = "单据状态(1-待装车 2-装车中 3-完成)", height = 20, width = 30,orderNum="") 
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

    @Transient
    @ApiModelProperty("装车单明细")
    private List<WmsOutDespatchOrderReJo> wmsOutDespatchOrderReJo;

    /**
     * 报关地点
     */
    @ApiModelProperty(name = "clearanceLocale",value = "报关地点")
    @Column(name = "clearance_locale")
    private String clearanceLocale;

    /**
     * 起运港
     */
    @ApiModelProperty(name = "originHarbor",value = "起运港")
    @Column(name = "origin_harbor")
    private String originHarbor;

    @Transient
    @ApiModelProperty(name = "deliveryOrders",value = "出货单明细")
    private List<WmsOutDeliveryOrder> deliveryOrders;

    private static final long serialVersionUID = 1L;
}