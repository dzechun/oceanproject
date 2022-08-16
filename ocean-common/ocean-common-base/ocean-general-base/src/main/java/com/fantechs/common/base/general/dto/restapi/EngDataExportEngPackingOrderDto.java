package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class EngDataExportEngPackingOrderDto implements Serializable {

    @Id
    @ApiModelProperty(name="packingOrderId",value = "装箱单ID")
    @Column(name = "packing_order_id")
    private String packingOrderId;

    @ApiModelProperty(name="option2",value = "PSGUID")
    @Column(name = "option2")
    private String option2;

    @ApiModelProperty(name="contractCode",value = "合同号")
    @Column(name = "contract_code")
    private String contractCode;

    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    @Column(name = "purchase_req_order_code")
    private String purchaseReqOrderCode;

    @ApiModelProperty(name="materialCode",value = "材料编码")
    @Column(name = "material_code")
    private String materialCode;

    @ApiModelProperty(name="locationNum",value = "位号")
    @Column(name = "location_num")
    private String locationNum;

    @ApiModelProperty(name="designQty",value = "设计量")
    @Column(name = "design_qty")
    private String designQty;

    @ApiModelProperty(name="surplusQty",value = "余量")
    @Column(name = "surplus_qty")
    private String surplusQty;

    @ApiModelProperty(name="purchaseReqQty",value = "请购量")
    @Column(name = "purchase_req_qty")
    private String purchaseReqQty;

    @ApiModelProperty(name="chQty",value = "采购量")
    @Column(name = "ch_qty")
    private String chQty;

    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Column(name = "dominant_term_code")
    private String dominantTermCode;

    @ApiModelProperty(name="deviceCode",value = "装置号")
    @Column(name = "device_code")
    private String deviceCode;

    @ApiModelProperty(name="materialPurpose",value = "材料用途")
    @Column(name = "material_purpose")
    private String materialPurpose;

    @ApiModelProperty(name="remark",value = "备注")
    @Column(name = "remark")
    private String remark;

    @ApiModelProperty(name="recordTime",value = "登记时间")
    @Column(name = "record_time")
    private String recordTime;

    @ApiModelProperty(name="recordUser",value = "登记人")
    @Column(name = "record_user")
    private String recordUser;

    @ApiModelProperty(name="materialName",value = "货物名称")
    @Column(name = "material_name")
    private String materialName;

    @ApiModelProperty(name="spec",value = "规格")
    @Column(name = "spec")
    private String spec;

    @ApiModelProperty(name="unitName",value = "单位")
    @Column(name = "unit_name")
    private String unitName;
}
