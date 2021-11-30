package com.fantechs.common.base.general.entity.srm.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
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
public class SearchSrmPlanDeliveryOrderDet extends BaseQuery implements Serializable {

    /**
     * 送货计划单ID
     */
    @ApiModelProperty(name="planDeliveryOrderId",value = "送货计划单ID")
    private Long planDeliveryOrderId;

    /**
     * 采购订单号
     */
    @ApiModelProperty(name="purchaseOrderDetId",value = "采购订单明细ID")
    private String purchaseOrderCode;

    /**
     * 收货仓库
     */
    @ApiModelProperty(name="warehouseName",value = "收货仓库")
    private String warehouseName;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    private String materialName;

    /**
     * 计划交货日期开始
     */
    @ApiModelProperty(name="planDeliveryDateStart",value = "计划交货日期开始")
    private String planDeliveryDateStart;

    /**
     * 计划交货日期结束
     */
    @ApiModelProperty(name="planDeliveryDateEnd",value = "计划交货日期结束")
    private String planDeliveryDateEnd;

    /**
     * 是否已经生成ASN(0-否 1-是)
     */
    @ApiModelProperty(name="ifCreateAsn",value = "是否已经生成ASN(0-否 1-是)")
    private Byte ifCreateAsn;

    /**
     * 送货计划单编码
     */
    @ApiModelProperty(name="planDeliveryOrderCode",value = "送货计划单编码")
    private String planDeliveryOrderCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    /**
     * 单据状态(1-未提交 2-提交)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-未提交 2-提交)")
    private Byte orderStatus;

    private static final long serialVersionUID = 1L;
}
