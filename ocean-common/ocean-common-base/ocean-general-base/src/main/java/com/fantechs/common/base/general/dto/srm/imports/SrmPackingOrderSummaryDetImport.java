package com.fantechs.common.base.general.dto.srm.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SrmPackingOrderSummaryDetImport implements Serializable {

    /**
     * 发运批次
     */
    @ApiModelProperty(name="despatchBatch",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="")
    private String despatchBatch;


    /**
     * 包装箱号
     */
    @ApiModelProperty(name="cartonCode",value = "包装箱号")
    @Excel(name = "包装箱号", height = 20, width = 30)
    private String cartonCode;


    /**
     * 内箱号
     */
    @ApiModelProperty(name="innerCartonCode",value = "内箱号")
    @Excel(name = "内箱号", height = 20, width = 30,orderNum="")
    private String innerCartonCode;

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
    @Excel(name = "装置码", height = 20, width = 30,orderNum="")
    private String deviceCode;

    /**
     * 位号
     */
    @ApiModelProperty(name="locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30)
    private String locationNum;

    /**
     * 材料编码
     */
    @ApiModelProperty(name="materialCode",value = "材料编码")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="")
    private String materialCode;

    /**
     * 原材料编码
     */
    @ApiModelProperty(name="rawMaterialCode",value = "原材料编码")
    @Excel(name = "原材料编码", height = 20, width = 30,orderNum="")
    private String rawMaterialCode;

    /**
     * 供货商名称
     */
    @ApiModelProperty(name="supplierName",value = "供货商名称")
    @Excel(name = "供货商名称", height = 20, width = 30)
    private String supplierName;

    /**
     * 图号
     */
    @ApiModelProperty(name="drawingNumber",value = "图号")
    @Excel(name = "图号", height = 20, width = 30,orderNum="")
    private String drawingNumber;

    /**
     * 件号
     */
    @ApiModelProperty(name="partNumber",value = "件号")
    @Excel(name = "件号", height = 20, width = 30,orderNum="")
    private String partNumber;


    /**
     * 规格
     */
    @ApiModelProperty(name="spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="")
    private String spec;

    /**
     * 数量
     */
    @ApiModelProperty(name="qty",value = "数量")
    @Excel(name = "数量", height = 20, width = 30,orderNum="")
    private BigDecimal qty;

    /**
     * 单位
     */
    @ApiModelProperty(name="unitName",value = "单位")
    @Excel(name = "单位", height = 20, width = 30,orderNum="")
    private String unitName;

    /**
     * 备注
     */
    @ApiModelProperty(name="remark",value = "备注")
    @Excel(name = "备注", height = 20, width = 30,orderNum="")
    private String remark;
}
