package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/9/24
 */
@Data
public class SearchProductionInStorage extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(name = "materialId",value = "物料")
    private Long materialId;

    @ApiModelProperty(name = "orgId",value = "组织")
    private Long orgId;

    @ApiModelProperty(name = "workShopId",value = "车间")
    private Long workShopId;

    @ApiModelProperty(name = "proId",value = "产线")
    private Long proId;

    @ApiModelProperty(name = "workOrderId",value = "工单")
    private Long workOrderId;

    @ApiModelProperty(name = "salesOrderId",value = "订单")
    private Long salesOrderId;

    private String workOrderCode;

    private String salesOrderCode;
}
