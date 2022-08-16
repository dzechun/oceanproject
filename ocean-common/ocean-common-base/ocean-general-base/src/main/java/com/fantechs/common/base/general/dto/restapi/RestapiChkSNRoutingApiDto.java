package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestapiChkSNRoutingApiDto implements Serializable {

    protected String proCode;  //产线编码
    protected String processCode;   //工序编码
    protected String barcodeCode; //成品SN
    protected String partBarcode; //半成品SN 部件条码
    protected String eamJigBarCode; //治具SN
    protected String equipmentBarCode; //设备条码
    protected String workOrderCode; //成品工单号
}
