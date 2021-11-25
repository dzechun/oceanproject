package com.fantechs.common.base.general.entity.ureport.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchSrmAsnOrder extends BaseQuery implements Serializable {

    /**
     * ASN单号
     */
    @ApiModelProperty(name = "asnOrderCode",value = "ASN单号")
    @Excel(name = "ASN单号", height = 20, width = 30,orderNum="1")
    private String asnOrderCode;

    /**
     * 采购订单号
     */
    @ApiModelProperty(name = "purchaseOrderCode",value = "采购订单号")
    @Excel(name = "采购订单号", height = 20, width = 30,orderNum="2")
    private String purchaseOrderCode;

    /**
     * 计划日期开始
     */
    @ApiModelProperty(name = "planDeliveryDateStart",value = "计划日期开始")
    private String planDeliveryDateStart;

    /**
     * 计划日期结束
     */
    @ApiModelProperty(name = "planDeliveryDateEnd",value = "计划日期结束")
    private String planDeliveryDateEnd;

    /**
     *收货仓库
     */
    @ApiModelProperty(name = "warehouseName",value = "收货仓库")
    private String warehouseName;

    /**
     *物料编码
     */
    @ApiModelProperty(name = "materialCode",value = "物料编码")
    private String  materialCode;

    /**
     *物料名称
     */
    @ApiModelProperty(name = "materialName",value = "物料名称")
    private String  materialName;

    /**
     * 物料版本
     */
    @ApiModelProperty(name="materialVersion",value = "物料版本")
    private String materialVersion;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     *单位
     */
    @ApiModelProperty(name = "unitName",value = "单位")
    private String  unitName;

    /**
     *SN码
     */
    @ApiModelProperty(name = "barcode",value = "SN码")
    private String  barcode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name="colorBoxCode",value = "彩盒号")
    private String colorBoxCode;

    /**
     * 箱码
     */
    @ApiModelProperty(name="cartonCode" ,value="箱码")
    private String cartonCode;

    /**
     *栈板码
     */
    @ApiModelProperty(name = "palletCode",value = "栈板码")
    private String  palletCode;

    /**
     * 批次号
     */
    @ApiModelProperty(name="batchCode" ,value="批次号")
    private String batchCode;

    /**
     * 生产日期开始
     */
    @ApiModelProperty(name="productionDateStart",value = "生产日期开始")
    private String productionDateStart;

    /**
     * 生产日期结束
     */
    @ApiModelProperty(name="productionDateEnd",value = "生产日期")
    private String productionDateEnd;

    /**
     *发货人
     */
    @ApiModelProperty(name = "shipperName",value = "发货人")
    private String  shipperName;

    /**
     * 发货日期开始
     */
    @ApiModelProperty(name="deliverDateStart",value = "发货日期开始")
    private String deliverDateStart;

    /**
     * 发货日期结束
     */
    @ApiModelProperty(name="deliverDateEnd",value = "发货日期结束")
    private String deliverDateEnd;

    /**
     * ASN状态
     */
    @ApiModelProperty(name="orderStatus" ,value="ASN状态")
    private Byte orderStatus;

}
