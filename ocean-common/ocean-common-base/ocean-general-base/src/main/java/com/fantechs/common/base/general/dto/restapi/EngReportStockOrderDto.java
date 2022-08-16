package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class EngReportStockOrderDto implements Serializable {

    @Id
    @ApiModelProperty(name="stockOrderDetId",value = "盘点单ID")
    private Long stockOrderDetId;

    @ApiModelProperty(name="option1",value = "option1")
    private String option1;

    @ApiModelProperty(name="option2",value = "option2")
    private String option2;

    @ApiModelProperty(name="DHGUID",value = "DHGUID")
    private String DHGUID;

    @ApiModelProperty(name="contractCode",value = "合同号")
    private String contractCode;

    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    private String purchaseReqOrderCode;

    @ApiModelProperty(name="materialCode",value = "材料编码")
    private String materialCode;

    @ApiModelProperty(name="locationNum",value = "位号")
    private String locationNum;

    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    private String dominantTermCode;

    @ApiModelProperty(name="deviceCode",value = "装置号")
    private String deviceCode;

    @ApiModelProperty(name="varianceQty",value = "差异数量")
    private String varianceQty; //变化量

    @ApiModelProperty(name="inventoryStatusName",value = "库存状态名称")
    private String inventoryStatusName;  //材料状态

    @ApiModelProperty(name="createTime",value = "登记时间")
    private String createTime;

    @ApiModelProperty(name="createUserName",value = "登记人")
    private String createUserName;

}
