package com.fantechs.common.base.general.dto.srm.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SrmPackingOrderSummaryImport implements Serializable {

    /**
     * 请购单号
     */
    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    @Excel(name = "请购单号", height = 20, width = 30)
    private String purchaseReqOrderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30)
    private String contractCode;


    /**
     * 装置码
     */
    @ApiModelProperty(name="deviceCode",value = "装置码")
    @Excel(name = "装置码", height = 20, width = 30)
    private String deviceCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30)
    private String locationNum;

    /**
     * 货物名称
     */
    @ApiModelProperty(name="materialName",value = "货物名称")
    @Excel(name = "货物名称", height = 20, width = 30)
    private String materialName;

    /**
     * 箱数
     */
    @ApiModelProperty(name="cartonQty",value = "箱数")
    @Excel(name = "箱数", height = 20, width = 30)
    private Integer cartonQty;

    /**
     * 箱号
     */
    @ApiModelProperty(name="cartonCode",value = "箱号")
    @Excel(name = "箱号", height = 20, width = 30)
    private String cartonCode;

    /**
     * 净重(KG)
     */
    @ApiModelProperty(name="netWeight",value = "净重(KG)")
    @Excel(name = "净重(KG)", height = 20, width = 30)
    private BigDecimal netWeight;

    /**
     * 毛重(KG)
     */
    @ApiModelProperty(name="grossWeight",value = "毛重(KG)")
    @Excel(name = "毛重(KG)", height = 20, width = 30)
    private BigDecimal grossWeight;

    /**
     * 长(cm)
     */
    @ApiModelProperty(name="length",value = "长(cm)")
    @Excel(name = "长(cm)", height = 20, width = 30)
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ApiModelProperty(name="width",value = "宽(cm)")
    @Excel(name = "宽(cm)", height = 20, width = 30)
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ApiModelProperty(name="height",value = "高(cm)")
    @Excel(name = "高(cm)", height = 20, width = 30)
    private BigDecimal height;

    /**
     * 体积(m3)
     */
    @ApiModelProperty(name="volume",value = "体积(m3)")
    @Excel(name = "体积(m3)", height = 20, width = 30)
    private BigDecimal volume;

    /**
     * 供应商名称
     */
    @ApiModelProperty(name="supplierName",value = "供应商名称")
    @Excel(name = "供应商名称", height = 20, width = 30)
    private String supplierName;

    /**
     * 装箱单号
     */
    @ApiModelProperty(name="packingOrderCode",value = "装箱单号")
    @Excel(name = "装箱单号", height = 20, width = 30)
    private String packingOrderCode;

}
