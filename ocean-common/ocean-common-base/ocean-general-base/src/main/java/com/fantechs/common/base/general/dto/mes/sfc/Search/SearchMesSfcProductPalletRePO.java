package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesSfcProductPalletRePO extends BaseQuery implements Serializable {

    /**
     * 产品条码
     */
    @ApiModelProperty(name = "barcode", value = "产品条码")
    private String barcode;

    /**
     * 销售条码
     */
    @ApiModelProperty(name="salesBarcode" ,value="销售条码")
    private String salesBarcode;

}
