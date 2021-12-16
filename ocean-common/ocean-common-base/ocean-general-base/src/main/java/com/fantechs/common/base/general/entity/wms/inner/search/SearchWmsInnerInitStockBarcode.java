package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@Data
public class SearchWmsInnerInitStockBarcode extends BaseQuery implements Serializable {
    private Long initStockDetId;

    private Long initStockId;

    private String materialCode;

    @ApiModelProperty(name = "inPlantBarcode",value = "厂内码")
    private String inPlantBarcode;

    @ApiModelProperty(name = "salesBarcode",value = "销售条码")
    private String salesBarcode;

    @ApiModelProperty(name = "clientBarcode",value = "客户条码")
    private String clientBarcode;
}
