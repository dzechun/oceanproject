package com.fantechs.common.base.general.dto.srm.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SrmInAsnOrderDetImport implements Serializable {

    /**
     * 采购订单号
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30)
    private String purchaseOrderCode;

    /**
     * 收货仓库
     */
    @ApiModelProperty(name="warehouseName",value = "收货仓库")
    @Excel(name = "收货仓库", height = 20, width = 30)
    private String warehouseName;


    /**
     * 物料编码
     */
    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(name="materialName",value = "物料名称")
    @Excel(name = "物料名称", height = 20, width = 30)
    private String materialName;

    /**
     * 交货数量
     */
    @ApiModelProperty(name="deliveryQty",value = "交货数量")
    @Excel(name = "交货数量", height = 20, width = 30,orderNum="")
    private BigDecimal deliveryQty;


}
