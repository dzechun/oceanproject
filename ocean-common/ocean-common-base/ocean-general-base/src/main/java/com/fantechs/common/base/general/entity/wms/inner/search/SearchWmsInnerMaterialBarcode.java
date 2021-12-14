package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchWmsInnerMaterialBarcode extends BaseQuery implements Serializable {

    /**
     * 来料条码ID
     */
    @ApiModelProperty(name="materialBarcodeId",value = "来料条码ID")
    private Long materialBarcodeId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 来料条码ID列表
     */
    @ApiModelProperty(name="materialBarcodeIdList",value = "来料条码ID列表")
    private List<Long> materialBarcodeIdList;

    private static final long serialVersionUID = 1L;
}
