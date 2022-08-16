package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchSiftWorkDto implements Serializable {

    /**
     * 入库库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "入库库位ID")
    private Long inStorageId;

    /**
     * 物料集合
     */
    @ApiModelProperty(value = "list",example = "物料集合")
    private List<InStorageMaterialDto> list;

    /**
     * 出库库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "出库库位ID")
    private Long outStorageId;
}
