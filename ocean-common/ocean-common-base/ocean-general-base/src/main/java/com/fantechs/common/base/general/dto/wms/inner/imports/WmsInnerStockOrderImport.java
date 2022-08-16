package com.fantechs.common.base.general.dto.wms.inner.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/10/14
 */
@Data
public class WmsInnerStockOrderImport implements Serializable {

    /**
     * 库位
     */
    @Excel(name = "库位",  height = 20, width = 30, orderNum="1")
    @ApiModelProperty(name="storageCode" ,value="库位")
    private String storageCode;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码",  height = 20, width = 30, orderNum="2")
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 包装单位
     */
    @Excel(name = "包装单位",  height = 20, width = 30, orderNum="3")
    @ApiModelProperty(name="packingUnitName" ,value="包装单位")
    private String packingUnitName;

    /**
     * 库存状态
     */
    @Excel(name = "库存状态",  height = 20, width = 30, orderNum="4")
    @ApiModelProperty(name="inventoryStatusName" ,value="库存状态")
    private String inventoryStatusName;

    /**
     * 盘点数量
     */
    @Excel(name = "盘点数量",  height = 20, width = 30, orderNum="5")
    @ApiModelProperty(name="stockQty" ,value="盘点数量")
    private BigDecimal stockQty;

    /**
     * 批次号
     */
    @Excel(name = "批次号",  height = 20, width = 30, orderNum="6")
    @ApiModelProperty(name="batchCode" ,value="批次号")
    private String batchCode;

    /**
     * 托盘号
     */
    @Excel(name = "托盘号",  height = 20, width = 30, orderNum="7")
    @ApiModelProperty(name="palletCode" ,value="托盘号")
    private String palletCode;

    /**
     * 请购单号
     */
    @Excel(name = "请购单号",  height = 20, width = 30, orderNum="8")
    @ApiModelProperty(name="purchaseReqOrderCode" ,value="请购单号")
    private String purchaseReqOrderCode;

    /**
     * 合同号
     */
    @Excel(name = "合同号",  height = 20, width = 30, orderNum="9")
    @ApiModelProperty(name="contractCode" ,value="合同号")
    private String contractCode;

    /**
     * 装置号
     */
    @Excel(name = "装置号",  height = 20, width = 30, orderNum="10")
    @ApiModelProperty(name="deviceCode" ,value="装置号")
    private String deviceCode;

    /**
     * 位号
     */
    @Excel(name = "位号",  height = 20, width = 30, orderNum="11")
    @ApiModelProperty(name="locationNum" ,value="位号")
    private String locationNum;

    /**
     * 主项号
     */
    @Excel(name = "主项号",  height = 20, width = 30, orderNum="12")
    @ApiModelProperty(name="dominantTermCode" ,value="主项号")
    private String dominantTermCode;

    /**
     * 材料用途
     */
    @Excel(name = "材料用途",  height = 20, width = 30, orderNum="13")
    @ApiModelProperty(name="materialPurpose" ,value="材料用途")
    private String materialPurpose;

    /**
     * 供应商
     */
    @Excel(name = "供应商",  height = 20, width = 30, orderNum="14")
    @ApiModelProperty(name="supplierName" ,value="供应商")
    private String supplierName;
}
