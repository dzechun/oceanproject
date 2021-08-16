package com.fantechs.common.base.general.dto.restapi.esop;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class EsopWorkOrderRes implements Serializable {

    @JSONField(name="code")
    protected String code; //半成品SN 部件条码
    @JSONField(name="product_model")
    protected String productModel; //成品SN
    @JSONField(name="product_name")
    protected String productName; //RFID
    @JSONField(name="num")
    protected String num;  //产线编码
    @JSONField(name="specification")
    protected String specification;   //工段编码
    @JSONField(name="specification_no")
    protected String specificationNo;   //工位编码
    @JSONField(name="customer")
    protected String customer;   //工序编码
    @JSONField(name="plan_dept_code")
    protected String planDeptCode; //员工编号
    @JSONField(name="order_date")
    protected String orderDate; //设备SN
    @JSONField(name="createman")
    protected String createman; //治具SN 001,002,003,004,006
    @JSONField(name="createdate")
    protected String createdate; //作业结果
    @JSONField(name="market_dept")
    protected String marketDept; //不良现象代码
    @JSONField(name="saleTo")
    protected String sale_to;   //工序编码
    @JSONField(name="color")
    protected String color; //员工编号
    @JSONField(name="safe_enum")
    protected String safeEnum; //设备SN
    @JSONField(name="customerModel")
    protected String customer_model; //治具SN 001,002,003,004,006
    @JSONField(name="sellerCode")
    protected String seller_code; //作业结果
    @JSONField(name="seller")
    protected String seller; //不良现象代码
    @JSONField(name="overseas_order_no")
    protected String overseasOrderNo; //不良现象代码
    @JSONField(name="overseas_qty")
    protected String overseasQty; //不良现象代码
    @JSONField(name="product_type")
    protected String productType; //不良现象代码
}
