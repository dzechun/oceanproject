package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class EngReportInnerJobOrderDto implements Serializable {

    @Id
    @ApiModelProperty(name="jobOrderDetId",value = "移位单ID")
    @Column(name = "job_order_det_id")
    private String jobOrderDetId;

    @ApiModelProperty(name="option1",value = "option1")
    @Column(name = "option1")
    private String option1; // PPGUID

    @ApiModelProperty(name="option2",value = "option2")
    @Column(name = "option2")
    private String option2;   //PSGUID

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

    @ApiModelProperty(name="dominantTermCode",value = "主项号")
    @Column(name = "dominant_term_code")
    private String dominantTermCode;

    @ApiModelProperty(name="deviceCode",value = "装置号")
    @Column(name = "device_code")
    private String deviceCode;

    @ApiModelProperty(name="planQty",value = "变化量")
    @Column(name = "plan_qty")
    private String planQty;

    @ApiModelProperty(name="oldStorageId",value = "旧库位id")
    @Column(name = "old_torageId")
    private String oldStorageId;  // 旧DHGUID

    @ApiModelProperty(name="newStorageId",value = "新库位id")
    @Column(name = "new_storageId")
    private String newStorageId;  //新DHGUID

    @ApiModelProperty(name="createTime",value = "登记时间")
    @Column(name = "create_time")
    private String createTime;

    @ApiModelProperty(name="createUserName",value = "登记人")
    @Column(name = "create_user_name")
    private String createUserName;

}
