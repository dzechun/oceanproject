package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestapiSNDataTransferApiDto implements Serializable {

    protected String partBarcode; //半成品SN 部件条码
    protected String barCode; //成品SN
    protected String rfidCode; //RFID
    protected String proCode;  //产线编码
    protected String sectionCode;   //工段编码
    protected String stationCode;   //工位编码
    protected String processCode;   //工序编码
    protected String userCode; //员工编号
    protected String equipmentBarCode; //设备SN
    protected String eamJigBarCode; //治具SN 001,002,003,004,006
    protected String opResult; //作业结果
    protected String badnessPhenotypeCode; //不良现象代码
    protected String workOrderCode;//工单号
    protected String passTime;//耗时

}
