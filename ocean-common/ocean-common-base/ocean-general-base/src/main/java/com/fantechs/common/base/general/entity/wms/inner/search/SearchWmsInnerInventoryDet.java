package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@Data
public class SearchWmsInnerInventoryDet extends BaseQuery implements Serializable {
    @ApiModelProperty("库位id")
    private Long storageId;
    @ApiModelProperty("物料id")
    private Long materialId;
    @ApiModelProperty("条码")
    private String barcode;
}
