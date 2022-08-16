package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcProductCartonDet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="productCartonId",value = "包箱号ID")
    private Long productCartonId;

    @ApiModelProperty(name="workOrderBarcodeId",value = "工单条码ID")
    private Long workOrderBarcodeId;
}
