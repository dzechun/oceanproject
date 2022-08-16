package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class WmsDataExportInnerJobOrderDto implements Serializable {

    @Id
    @ApiModelProperty(name="packingOrderSummaryDetId",value = "装箱单明细ID")
    @Column(name = "packing_order_summary_det_id")
    private String packingOrderSummaryDetId;

    @ApiModelProperty(name="option1",value = "PPGUID")
    @Column(name = "option1")
    private String option1;

    @ApiModelProperty(name="contractCode",value = "合同号")
    @Column(name = "contract_code")
    private String contractCode;

    @ApiModelProperty(name="purchaseReqOrderCode",value = "请购单号")
    @Column(name = "purchase_req_order_code")
    private String purchaseReqOrderCode;

    @ApiModelProperty(name="despatchBatch",value = "物流批次号")
    @Column(name = "despatch_batch")
    private String despatchBatch;

    @ApiModelProperty(name="cartonCode",value = "包装箱号")
    @Column(name = "carton_code")
    private String cartonCode;

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

    @ApiModelProperty(name="putawayQty",value = "到货量")
    @Column(name = "putaway_qty")
    private String putawayQty;

    @ApiModelProperty(name="storageCode",value = "货架编号")
    @Column(name = "storage_code")
    private String storageCode;

    @ApiModelProperty(name="remark",value = "到货备注")
    @Column(name = "remark")
    private String remark;

    @ApiModelProperty(name="recordTime",value = "登记时间")
    @Column(name = "record_time")
    private String recordTime;

    @ApiModelProperty(name="recordUser",value = "登记人")
    @Column(name = "record_user")
    private String recordUser;

    @ApiModelProperty(name="innerTime",value = "入库校验时间")
    @Column(name = "inner_time")
    private String innerTime;

    @ApiModelProperty(name="innerSureTime",value = "入库确认时间")
    @Column(name = "inner_sure_time")
    private String innerSureTime;

}
