package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcProductPalletDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="productPalletId",value = "栈板ID")
    private Long productPalletId;

    @ApiModelProperty(name="workOrderBarcodeId",value = "工单条码ID")
    private Long workOrderBarcodeId;

    @ApiModelProperty(name="groupBy",value = "分组参数")
    private String groupBy;
}
