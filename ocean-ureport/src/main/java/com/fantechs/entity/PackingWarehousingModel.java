package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Data
public class PackingWarehousingModel implements Serializable {

    /**
     *发运批次
     */
    @ApiModelProperty(name = "despatchBatch",value = "发运批次")
    @Excel(name = "发运批次", height = 20, width = 30,orderNum="1")
    private String despatchBatch;

    /**
     *专业
     */
    @ApiModelProperty(name = "professionName",value = "专业")
    @Excel(name = "专业", height = 20, width = 30,orderNum="2")
    private String professionName;

    /**
     *合同号
     */
    @ApiModelProperty(name = "contractCode",value = "合同号")
    @Excel(name = "合同号", height = 20, width = 30,orderNum="3")
    private String contractCode;

    /**
     *请购单号
     */
    @ApiModelProperty(name = "purchaseReqOrderCode",value = "请购单号")
    @Excel(name = "请购单号", height = 20, width = 30,orderNum="4")
    private String purchaseReqOrderCode;

    /**
     *材料编码
     */
    @ApiModelProperty(name = "materialCode",value = "材料编码")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="5")
    private String materialCode;

    /**
     *位号
     */
    @ApiModelProperty(name = "locationNum",value = "位号")
    @Excel(name = "位号", height = 20, width = 30,orderNum="6")
    private String locationNum;

    /**
     *材料名称
     */
    @ApiModelProperty(name = "materialName",value = "材料名称")
    @Excel(name = "材料名称", height = 20, width = 30,orderNum="7")
    private String materialName;

    /**
     *规格
     */
    @ApiModelProperty(name = "spec",value = "规格")
    @Excel(name = "规格", height = 20, width = 30,orderNum="8")
    private String spec;

    /**
     *主项号
     */
    @ApiModelProperty(name = "dominantTermCode",value = "主项号")
    @Excel(name = "主项号", height = 20, width = 30,orderNum="9")
    private String dominantTermCode;

    /**
     *装置号
     */
    @ApiModelProperty(name = "deviceCode",value = "装置号")
    @Excel(name = "装置号", height = 20, width = 30,orderNum="10")
    private String deviceCode;

    /**
     *到货量
     */
    @ApiModelProperty(name = "putawayQty",value = "到货量")
    @Excel(name = "到货量", height = 20, width = 30,orderNum="11",type = 10,numFormat = "0.00")
    private BigDecimal putawayQty;

    /**
     *计量单位
     */
    @ApiModelProperty(name = "unitName",value = "计量单位")
    @Excel(name = "计量单位", height = 20, width = 30,orderNum="12")
    private String unitName;

    /**
     *材料用途
     */
    @ApiModelProperty(name = "materialPurpose",value = "材料用途")
    @Excel(name = "材料用途", height = 20, width = 30,orderNum="13")
    private String materialPurpose;

    /**
     *供应商
     */
    @ApiModelProperty(name = "supplierName",value = "供应商")
    @Excel(name = "供应商", height = 20, width = 30,orderNum="14")
    private String supplierName;
}
