package com.fantechs.common.base.general.dto.srm.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SrmPlanDeliveryOrderImport implements Serializable {

    /**
     * 送货计划标识
     */
    @ApiModelProperty(name="id",value = "送货计划标识")
    @Excel(name = "送货计划标识", height = 20, width = 30)
    private String id;

    /**
     * 供应商编码
     */
    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    @Excel(name = "供应商编码", height = 20, width = 30)
    private String supplierCode;

    /**
     * 采购订单号
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单明细ID")
    @Excel(name = "采购订单号", height = 20, width = 30)
    private String purchaseOrderCode;

    /**
     * 计划交货日期
     */
    @ApiModelProperty(name="planDeliveryDate",value = "计划交货日期")
    @Excel(name = "计划交货日期", height = 20, width = 30,format = "yyyy-MM-dd HH:mm:ss")
    private Date planDeliveryDate;

    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 计划交货数量
     */
    @ApiModelProperty(name="planDeliveryQty",value = "计划交货数量")
    @Excel(name = "计划交货数量", height = 20, width = 30)
    private BigDecimal planDeliveryQty;

    private static final long serialVersionUID = 1L;
}
