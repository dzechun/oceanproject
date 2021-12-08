package com.fantechs.common.base.general.dto.srm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
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

@Data
public class SrmPlanDeliveryOrderDetDto extends SrmPlanDeliveryOrderDet implements Serializable {

    /**
     * 送货计划单号
     */
    @Transient
    @Excel(name = "送货计划单号", height = 20, width = 30,orderNum="1")
    @ApiModelProperty(name="planDeliveryOrderCode",value = "送货计划单号")
    private String planDeliveryOrderCode;

    /**
     * 供应商名称
     */
    @Transient
    @Excel(name = "供应商名称", height = 20, width = 30,orderNum="2")
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 采购订单ID
     */
    @Transient
    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    private Long purchaseOrderId;

    /**
     * 采购订单号
     */
    @Transient
    @Excel(name = "采购订单号", height = 20, width = 30,orderNum="3")
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    private String purchaseOrderCode;

    /**
     * 收货仓库ID
     */
    @Transient
    @ApiModelProperty(name="warehouseId",value = "收货仓库ID")
    private Long warehouseId;

    /**
     * 收货仓库
     */
    @Transient
    @Excel(name = "收货仓库", height = 20, width = 30,orderNum="5")
    @ApiModelProperty(name="warehouseName",value = "收货仓库")
    private String warehouseName;

    /**
     * 物料ID
     */

    @Transient
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料编码
     */
    @Transient
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="6")
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @Excel(name = "物料名称", height = 20, width = 30,orderNum="7")
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 物料版本
     */
    @Transient
    @Excel(name = "物料版本", height = 20, width = 30,orderNum="8")
    @ApiModelProperty(name="materialVersion",value = "物料版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @Excel(name = "物料描述", height = 20, width = 30,orderNum="9")
    @ApiModelProperty(name="materialDesc",value = "物料描述")
    private String materialDesc;

    /**
     * 单位
     */
    @Transient
    @Excel(name = "单位", height = 20, width = 30,orderNum="10")
    @ApiModelProperty(name="unitName",value = "单位")
    private String unitName;

    /**
     * 采购数量
     */
    @Transient
    @Excel(name = "采购数量", height = 20, width = 30,orderNum="11")
    @ApiModelProperty(name="orderQty",value = "采购数量")
    private BigDecimal orderQty;

    /**
     * 累计交货数量
     */
    @Transient
    @Excel(name = "累计交货数量", height = 20, width = 30,orderNum="12")
    @ApiModelProperty(name="totalReceivingQty",value = "累计交货数量")
    private BigDecimal totalReceivingQty;

    /**
     * 累计计划送货数量
     */
    @Transient
    @Excel(name = "累计计划送货数量", height = 20, width = 30,orderNum="13")
    @ApiModelProperty(name="totalPlanDeliveryQty",value = "累计计划送货数量")
    private BigDecimal totalPlanDeliveryQty;


    private static final long serialVersionUID = 1L;
}
