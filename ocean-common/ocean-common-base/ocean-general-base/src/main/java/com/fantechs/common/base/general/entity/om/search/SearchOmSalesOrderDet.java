package com.fantechs.common.base.general.entity.om.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchOmSalesOrderDet extends BaseQuery implements Serializable {
    /**
     * 销售订单ID
     */
    @ApiModelProperty(name="salesOrderId",value = "销售订单ID")
    private Long salesOrderId;

    @ApiModelProperty(name="salesOrderDetId",value = "销售订单明细ID")
    private Long salesOrderDetId;

    @ApiModelProperty(name = "materialCode", value = "物料编码")
    private String materialCode;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    @Excel(name = "物料ID", height = 20, width = 30,orderNum="")
    private Long materialId;

    @ApiModelProperty(name="salesOrderCode",value = "销售订单编码")
    private String salesOrderCode;

    @ApiModelProperty(name="salesCode",value = "销售编码")
    private String salesCode;

    /**
     * 销售订单ID列表
     */
    @ApiModelProperty(name="salesOrderIdList",value = "销售订单ID列表")
    private List<Long> salesOrderIdList;

    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    private Integer ifAllIssued;

    /**
     * 已发货订单(1为已发货)
     * */
    @ApiModelProperty(name = "notNullActualQty", value = "已发货订单(0为未发货 1为已发货)")
    private Integer notNullActualQty;

    /**
     * 是否已全部退货(0-否 1-是)
     * */
    @ApiModelProperty(name="ifAllReturn",value = "是否已全部退货(0-否 1-是)")
    private Integer ifAllReturn;
}
