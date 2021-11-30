package com.fantechs.common.base.general.dto.srm.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class SrmInAsnOrderDetBarcodeImport implements Serializable {

    /**
     * 采购订单号
     */
    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30)
    private String purchaseOrderCode;

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
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30)
    private BigDecimal qty;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode",value = "批次号")
    @Excel(name = "批次号", height = 20, width = 30)
    private String batchCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name="productionDate",value = "生产日期")
    @Excel(name = "生产日期", height = 20, width = 30)
    private Date productionDate;

    /**
     * SN码
     */
    @ApiModelProperty(name="barcode",value = "SN码")
    @Excel(name = "SN码", height = 20, width = 30)
    private String barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    @Excel(name = "彩盒号", height = 20, width = 30)
    private String colorBoxCode;

    /**
     * 箱码
     */
    @ApiModelProperty(name="cartonCode",value = "箱码")
    @Excel(name = "箱码", height = 20, width = 30)
    private String cartonCode;

    /**
     * 栈板码
     */
    @ApiModelProperty(name="palletCode",value = "栈板码")
    @Excel(name = "栈板码", height = 20, width = 30)
    private String palletCode;

}
