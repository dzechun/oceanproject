package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/8/27
 */
@Data
public class SearchFindInvStorage extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "warehouseId",value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(name = "materialId",value = "物料id")
    private Long materialId;

    @ApiModelProperty(name = "batchCode",value = "批次")
    private String batchCode;

    @ApiModelProperty(name = "storageType",value = "库位类型")
    private Byte storageType;
}
