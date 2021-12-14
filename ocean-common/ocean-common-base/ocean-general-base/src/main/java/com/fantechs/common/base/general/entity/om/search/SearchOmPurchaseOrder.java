package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/15
 */
@Data
public class SearchOmPurchaseOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单编码")
    private String purchaseOrderCode;

    @ApiModelProperty(name="supplierCode",value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(name="supplierName",value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(name="warehouseName",value = "仓库名称")
    private String warehouseName;


    @ApiModelProperty(name="orderStatus",value = "订单状态")
    private Byte orderStatus;

    @ApiModelProperty(name="status",value = "状态（0、无效 1、有效）")
    private Byte status;

    @ApiModelProperty(name="userId",value = "用户ID")
    private Long userId;

    @ApiModelProperty(name="ifSupplierFind",value = "是否组织查询（0、否 1、是）")
    private Byte ifSupplierFind;

    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;
}
