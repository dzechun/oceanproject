package com.fantechs.common.base.general.dto.om.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class SmtOrderImport implements Serializable {

    /**
     * 客货号
     */
    @Excel(name = "客货号(必填)", height = 20, width = 30)
    @ApiModelProperty(name="freightNum" ,value="客货号")
    private String freightNum;

    /**
     * 合同交期
     */
    @Excel(name = "合同交期", height = 20, width = 30,importFormat = "yyyy-MM-dd")
    @ApiModelProperty(name="contractDate" ,value="合同交期")
    private Date contractDate;

    /**
     * 订单合同号
     */
    @ApiModelProperty(name="contractCode" ,value="订单合同号")
    @Excel(name = "订单合同号(必填)", height = 20, width = 30)
    private String contractCode;

    /**
     * 客户ID
     */
    @ApiModelProperty(name="supplierId" ,value="客户id")
    private Long supplierId;

    /**
     * 客户编码
     */
    @Excel(name = "客户编码(必填)", height = 20, width = 30)
    @ApiModelProperty(name="supplierCode" ,value="客户编码")
    private String supplierCode;

    /**
     * 下单日期
     */
    @ApiModelProperty(name="order_date" ,value="下单日期")
    @Excel(name = "下单日期", height = 20, width = 30,importFormat = "yyyy-MM-dd")
    private Date orderDate;

    /**
     * 排产日期
     */
    @ApiModelProperty(name="schedule_date" ,value="排产交期")
    @Excel(name = "排产日期", height = 20, width = 30,importFormat = "yyyy-MM-dd")
    private Date scheduleDate;

    /**
     * 订单交货日期
     */
    @ApiModelProperty(name="deliveryDate" ,value="订单交货日期")
    @Excel(name = "订单交货日期", height = 20, width = 30,importFormat = "yyyy-MM-dd")
    private Date deliveryDate;

    //=====================================================================
    // 销售订单明细字段

    /**
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号id")
    private Long materialId;

    /**
     * 产品料号
     */
    @Excel(name = "产品料号(必填)", height = 20, width = 30)
    @ApiModelProperty(name="materialCode" ,value="产品料号")
    private String materialCode;

    /**
     * 产品数量
     */
    @ApiModelProperty(value = "产品数量",example = "产品数量")
    @Excel(name = "产品数量")
    private java.math.BigDecimal total;

//    /**
//     * 编箱号
//     */
//    @ApiModelProperty(value = "编箱号",example = "编箱号")
//    @Excel(name = "编箱号")
//    private String boxCode;

    /**
     * 包装规格id
     */
    @ApiModelProperty(value = "包装规格id",example = "包装规格id")
    @Excel(name = "包装规格id")
    private Long packageSpecificationId;

    /**
     * 包装规格编码
     */
    @ApiModelProperty(value = "包装规格编码",example = "包装规格编码")
    @Excel(name = "包装规格编码")
    private String packageSpecificationCode;

    /**
     * 尺码(CM)
     */
    @ApiModelProperty(value = "尺码(CM)",example = "尺码(CM)")
    @Excel(name = "尺码(CM)")
    private java.math.BigDecimal size;

    /**
     * 毛重(KG)
     */
    @ApiModelProperty(value = "毛重(KG)",example = "毛重(KG)")
    @Excel(name = "毛重(KG)")
    private java.math.BigDecimal roughWeight;

    /**
     * 净重(KG)
     */
    @ApiModelProperty(value = "净重(KG)",example = "净重(KG)")
    @Excel(name = "净重(KG)")
    private java.math.BigDecimal netWeight;

    /**
     * 产品规格文件路径
     */
    @ApiModelProperty(value = "产品规格文件",example = "产品规格文件")
    @Excel(name = "产品规格文件路径")
    private String specFile;
}
