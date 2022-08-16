package com.fantechs.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
@Data
public class MonthInOutModel implements Serializable {

    /**
     * 业务员
     */
    @ApiModelProperty(name = "salesUserName",value = "业务员")
    @Excel(name = "业务员",height = 20, width = 30,orderNum="1")
    private String salesUserName;

    /**
     * 大区
     */
    @ApiModelProperty(name = "regionName",value = "大区")
    @Excel(name = "大区",height = 20, width = 30,orderNum="2")
    private String regionName;

    /**
     * 国家
     */
    @ApiModelProperty(name = "countryName",value = "国家")
    @Excel(name = "国家",height = 20, width = 30,orderNum="3")
    private String countryName;

    /**
     * 客户id
     */
    @ApiModelProperty(name = "supplierId",value = "客户")
    private String supplierId;

    /**
     * 客户
     */
    @ApiModelProperty(name = "supplierName",value = "客户")
    @Excel(name = "客户",height = 20, width = 30,orderNum="4")
    private String supplierName;


    /**
     * 品牌
     */
    @ApiModelProperty(name = "brandName",value = "品牌")
    @Excel(name = "品牌",height = 20, width = 30,orderNum="5")
    private String brandName;

    /**
     * 产品分类
     */
    @ApiModelProperty(name = "productCategory",value = "产品分类")
    @Excel(name = "产品分类",height = 20, width = 30,orderNum="6")
    private String productCategory;

    /**
     * 型号
     */
    @ApiModelProperty(name = "productModelCode",value = "型号")
    @Excel(name = "型号",height = 20, width = 30,orderNum="7")
    private String productModelCode;

    /**
     * 产品id
     */
    @ApiModelProperty(name = "materialId",value = "产品id")
    private String materialId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name = "materialCode",value = "产品编码")
    @Excel(name = "产品编码",height = 20, width = 30,orderNum="8")
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    @Excel(name = "产品描述",height = 20, width = 30,orderNum="9")
    private String materialDesc;
}
