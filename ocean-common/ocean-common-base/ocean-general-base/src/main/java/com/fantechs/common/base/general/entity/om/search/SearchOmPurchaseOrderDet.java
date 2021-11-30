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
public class SearchOmPurchaseOrderDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="purchaseOrderId",value = "采购订单ID")
    private Long purchaseOrderId;

    @ApiModelProperty(name="purchaseOrderDetId",value = "采购订单明细ID")
    private Long purchaseOrderDetId;

    @ApiModelProperty(name="purchaseOrderCode",value = "采购订单号")
    private String purchaseOrderCode;

    @ApiModelProperty(name="materialCode",value = "物料编码")
    private String materialCode;

    /**
     * 编码查询标记(设为1做等值查询)
     */
    @ApiModelProperty(name = "codeQueryMark",value = "编码查询标记(设为1做等值查询)")
    private Integer codeQueryMark;

}
