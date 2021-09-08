package com.fantechs.common.base.general.dto.restapi;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataExportEngPackingOrderDto implements Serializable {

    protected String PSGUID; //PSGUID
    protected String 材料编码; //material_code
    protected String 位号; //location_num
    protected String 设计量;  //design_qty
    protected String 余量;   //surplus_qty
    protected String 请购量;   //purchase_req_qty
    protected String 采购量;   //ch_qty
    protected String 主项号; //dominant_term_code
    protected String 装置号; //device_code
    protected String 材料用途; //material_purpose
    protected String 备注; //remark
    protected String 登记时间; //登记时间
    protected String 登记人;//登记人

}
