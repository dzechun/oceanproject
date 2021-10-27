package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Data
public class PackingOutboundModel implements Serializable {

    /**
     *专业
     */
    @ApiModelProperty(name = "专业",value = "professionName")
    @Excel(name = "专业", height = 20, width = 30,orderNum="1")
    private String professionName;

    /**
     *领料单号
     */
    @ApiModelProperty(name = "领料单号",value = "deliveryOrderCode")
    @Excel(name = "领料单号", height = 20, width = 30,orderNum="2")
    private String deliveryOrderCode;

    /**
     *领料单位
     */
    @ApiModelProperty(name = "领料单位",value = "customerName")
    @Excel(name = "领料单位", height = 20, width = 30,orderNum="3")
    private String customerName;

    /**
     *领料时间
     */
    @ApiModelProperty(name = "领料时间",value = "orderDate")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "领料时间", height = 20, width = 30,orderNum="4",databaseFormat = "yyyy-MM-dd HH:mm:ss")
    private Data orderDate;

    /**
     *材料编码
     */
    @ApiModelProperty(name = "材料编码",value = "materialCode")
    @Excel(name = "材料编码", height = 20, width = 30,orderNum="5")
    private String materialCode;

    /**
     *位号
     */
    @ApiModelProperty(name = "位号",value = "locationNum")
    @Excel(name = "位号", height = 20, width = 30,orderNum="6")
    private String locationNum;

    /**
     *装置号
     */
    @ApiModelProperty(name = "装置号",value = "deviceCode")
    @Excel(name = "装置号", height = 20, width = 30,orderNum="7")
    private String deviceCode;

    /**
     *申请量
     */
    @ApiModelProperty(name = "申请量",value = "packingQty")
    @Excel(name = "申请量", height = 20, width = 30,orderNum="8",type = 10,numFormat = "0.00")
    private BigDecimal packingQty;

    /**
     *批准量
     */
    @ApiModelProperty(name = "批准量",value = "pickingQty")
    @Excel(name = "批准量", height = 20, width = 30,orderNum="9",type = 10,numFormat = "0.00")
    private BigDecimal pickingQty;

    /**
     *实发量
     */
    @ApiModelProperty(name = "实发量",value = "dispatchQty")
    @Excel(name = "实发量", height = 20, width = 30,orderNum="10",type = 10,numFormat = "0.00")
    private BigDecimal dispatchQty;

    /**
     *管线号
     */
    @ApiModelProperty(name = "管线号",value = "pipelineNumber")
    @Excel(name = "管线号", height = 20, width = 30,orderNum="11")
    private String pipelineNumber;

    /**
     *规格
     */
    @ApiModelProperty(name = "规格",value = "spec")
    @Excel(name = "规格", height = 20, width = 30,orderNum="12")
    private String spec;

    /**
     *材料名称
     */
    @ApiModelProperty(name = "材料名称",value = "materialName")
    @Excel(name = "材料名称", height = 20, width = 30,orderNum="13")
    private String materialName;

    /**
     *领料人
     */
    @ApiModelProperty(name = "领料人",value = "pickMaterialUserName")
    @Excel(name = "领料人", height = 20, width = 30,orderNum="14")
    private String pickMaterialUserName;

    /**
     *审批人
     */
    @ApiModelProperty(name = "审批人",value = "auditUserName")
    @Excel(name = "审批人", height = 20, width = 30,orderNum="15")
    private String auditUserName;

    /**
     *发料人
     */
    @ApiModelProperty(name = "发料人",value = "issueUserName")
    @Excel(name = "发料人", height = 20, width = 30,orderNum="16")
    private String  issueUserName;
}
