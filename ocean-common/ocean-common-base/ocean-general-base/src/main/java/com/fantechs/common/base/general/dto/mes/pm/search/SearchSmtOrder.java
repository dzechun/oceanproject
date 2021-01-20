package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtOrder extends BaseQuery implements Serializable {

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
     * 产品料号ID
     */
    @ApiModelProperty(name="materialId" ,value="产品料号ID")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;

    /**
     * 客户名称
     */
    @ApiModelProperty(name="supplierName" ,value="客户名称")
    private String supplierName;

    /**
     * 状态（0、创建 1、下载完成 2、已完工 3、作废）
     */
    @ApiModelProperty(name="status" ,value="状态（0、创建 1、下载完成 2、已完工 3、作废）")
    private Byte status;
}
