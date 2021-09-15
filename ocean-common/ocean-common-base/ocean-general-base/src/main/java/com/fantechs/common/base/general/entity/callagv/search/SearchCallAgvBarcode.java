package com.fantechs.common.base.general.entity.callagv.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchCallAgvBarcode extends BaseQuery implements Serializable {

    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;
}
