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
public class SearchOmInStorage extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(name = "materialId",value = "物料")
    private Long materialId;

    @ApiModelProperty(name = "orgId",value = "组织")
    private Long orgId;

    @ApiModelProperty(name = "salesOrderId",value = "订单")
    private Long salesOrderId;

    @ApiModelProperty(name = "supplierId",value = "客户")
    private Long supplierId;

    private String workOrderCode;

    private String salesOrderCode;

    private String materialName;

    private String packingUnitName;
}
