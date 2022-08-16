package com.fantechs.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShipmentDetDto implements Serializable {

    @ApiModelProperty(name="deliveryOrderCode",value = "出货单号")
    @Excel(name = "出货单号", height = 20, width = 30,orderNum="1")
    private String deliveryOrderCode;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    @Excel(name = "物料编码", height = 20, width = 30,orderNum="2")
    private String materialCode;

    @ApiModelProperty(name = "customerName",value ="客户名称" )
    @Excel(name = "客户名称", height = 20, width = 30,orderNum="3")
    private String customerName;

    @ApiModelProperty(name="salesManName" ,value="业务员名称")
    @Excel(name = "业务员名称", height = 20, width = 30,orderNum="4")
    private String salesManName;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    @Excel(name = "销售编码", height = 20, width = 30,orderNum="5")
    private String salesCode;

    @ApiModelProperty(name="actualDespatchDate",value = "实际发车时间")
    @Excel(name = "实际发车时间", height = 20, width = 30,orderNum="6",exportFormat ="yyyy-MM-dd HH:mm:ss")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date actualDespatchDate;

    @ApiModelProperty(name="containerNumber",value = "柜号")
    @Excel(name = "柜号", height = 20, width = 30,orderNum="7")
    private String containerNumber;

    @ApiModelProperty(name = "clearanceLocale",value = "报关地点")
    @Excel(name = "报关地点", height = 20, width = 30,orderNum="8")
    private String clearanceLocale;

    @ApiModelProperty(name = "originHarbor",value = "起运港")
    @Excel(name = "起运港", height = 20, width = 30,orderNum="9")
    private String originHarbor;

    @ApiModelProperty(name="shipmentEnterpriseName" ,value="物流商名称")
    @Excel(name = "物流商名称", height = 20, width = 30,orderNum="10")
    private String shipmentEnterpriseName;

    @ApiModelProperty(name="transportCategoryId" ,value="运输类型(1-快递 2-海运 3-空运)")
    @Excel(name = "运输类型(1-快递 2-海运 3-空运)", height = 20, width = 30,orderNum="11", replace = {"快递_1","海运_2","空运_3"})
    private Integer transportCategoryId;

    @ApiModelProperty(name="shipmentType" ,value="出货类型")
    @Excel(name = "出货类型", height = 20, width = 30,orderNum="12")
    private String shipmentType;

    @ApiModelProperty(name="loadingType" ,value="装柜类型")
    @Excel(name = "装柜类型", height = 20, width = 30,orderNum="13")
    private String loadingType;

    @ApiModelProperty(name="qty" ,value="实发数量")
    @Excel(name = "实发数量", height = 20, width = 30,orderNum="14")
    private BigDecimal qty;

}
