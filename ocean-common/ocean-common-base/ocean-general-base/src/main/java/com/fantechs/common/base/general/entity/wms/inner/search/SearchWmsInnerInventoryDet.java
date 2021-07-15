package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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
    @ApiModelProperty("相关单号")
    private String relevanceOrderCode;
    @ApiModelProperty("物料数量")
    private BigDecimal materialQty;
    @ApiModelProperty("是否是不相等 -- 0、相等 1、不相等")
    private Integer notEqualMark;
}
