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
public class SearchMesSfcBarcodeProcess extends BaseQuery implements Serializable {

    /**
     * 产品条码
     */
    @ApiModelProperty(name = "barcode", value = "产品条码")
    private String barcode;

    /**
     * 包箱号
     */
    @ApiModelProperty(name = "cartonCode", value = "包箱号")
    private String cartonCode;

    /**
     * 栈板号
     */
    @ApiModelProperty(name = "palletCode", value = "栈板号")
    private String palletCode;

    /**
     * 彩盒号
     */
    @ApiModelProperty(name = "colorBoxCode", value = "彩盒号")
    private String colorBoxCode;

    /**
     * 工单ID
     */
    @ApiModelProperty(name = "workOrderId", value = "工单ID")
    private Long workOrderId;

    /**
     * 产品物料ID
     */
    @ApiModelProperty(name = "materialId", value = "产品物料ID")
    private Long materialId;

    @ApiModelProperty(name = "isCustomerBarcode",value = "客户条码精确查找")
    private String isCustomerBarcode;
}
