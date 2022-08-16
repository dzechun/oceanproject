package com.fantechs.common.base.general.dto.om;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/30
 */
@Data
public class FindOrderMaterialDto implements Serializable {
    /**
     * 订单ID
     */
    @ApiModelProperty(name="orderId" ,value="订单ID")
    private Long orderId;
    /**
     * 订单号
     */
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    /**
     * 合同号
     */
    @ApiModelProperty(name="contractCode" ,value="合同号")
    private String contractCode;
    /**
     * 订单数量
     */
    @ApiModelProperty(name="orderQuantity" ,value="订单数量")
    private Integer orderQuantity;

    /**
     * 销售订单与物料id
     */
    @ApiModelProperty(value = "销售订单与物料id",example = "销售订单与物料id")
    private Long orderMaterialId;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id",example = "物料id")
    private Long materialId;

    /**
     * 产品数量
     */
    @ApiModelProperty(value = "产品数量",example = "产品数量")
    private java.math.BigDecimal total;

    /**
     * 产品型号
     */
    @ApiModelProperty(value = "产品型号",example = "产品型号")
    private String productModelName;

    /**
     * 产品料号
     */
    @ApiModelProperty(value = "产品料号",example = "产品料号")
    private String materialCode;
    /**
     * 产品版本
     */
    @ApiModelProperty(value = "产品版本",example = "产品版本")
    private String version;
    /**
     * 产品描述
     */
    @ApiModelProperty(value = "产品描述",example = "产品描述")
    private String materialDesc;
}
