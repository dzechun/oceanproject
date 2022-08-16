package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestapiPurchaseOrderApiDto implements Serializable {

    protected String BSART;  //凭证类型
    protected String LOEKZ;   //删除标识
    protected String EBELN; //采购凭证
    protected String EBELP; //采购凭证项目号
    protected String EPSTP;   //项目类别
    protected String LIFNR;   //供应商代码
    protected String AEDAT;   //创建日期
    protected String MATNR;   //物料号
    protected String TXZ01;   //物料描述
    protected String MENGE;   //采购订单数量
    protected String MEINS;   //订单单位
    protected String WERKS;   //工厂
    protected String LGORT;    //库存地点
    protected String UMSON;  //免费项目
    protected String RETPO;   //退货项目

}
