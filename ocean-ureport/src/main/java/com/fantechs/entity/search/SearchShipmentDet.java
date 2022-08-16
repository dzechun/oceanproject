package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchShipmentDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="deliveryOrderCode",value = "出货单号")
    private String deliveryOrderCode;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    @ApiModelProperty(name = "customerName",value ="客户名称" )
    private String customerName;

    @ApiModelProperty(name="salesManName" ,value="业务员名称")
    private String salesManName;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    @ApiModelProperty(name="actualDespatchTime",value = "实际发车时间")
    private String actualDespatchTime;

    @ApiModelProperty(name="containerNumber",value = "柜号")
    private String containerNumber;

    @ApiModelProperty(name = "clearanceLocale",value = "报关地点")
    private String clearanceLocale;

    @ApiModelProperty(name = "originHarbor",value = "起运港")
    private String originHarbor;

    @ApiModelProperty(name="shipmentEnterpriseName" ,value="物流商名称")
    private String shipmentEnterpriseName;

    @ApiModelProperty(name="transportCategoryId" ,value="运输类型(1-快递 2-海运 3-空运)")
    private Integer transportCategoryId;
}
