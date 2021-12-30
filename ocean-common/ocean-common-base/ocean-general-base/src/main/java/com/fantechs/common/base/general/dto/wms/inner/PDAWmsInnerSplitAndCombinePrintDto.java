package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PDAWmsInnerSplitAndCombinePrintDto implements Serializable {

    /**
     * 类型（1-分包箱 2-合包箱 3-分栈板 4-合栈板）
     */
    @ApiModelProperty(name="type",value = "类型（1-分包箱 2-合包箱 3-分栈板 4-合栈板）")
    private Byte type;

    /**
     * 库位id
     */
    @ApiModelProperty(name="storageId",value = "库位id")
    private Long storageId;

    /**
     * 物料id
     */
    @ApiModelProperty(name="materialId",value = "物料id")
    private Long materialId;


    /**
     * 扫描的条码id
     */
    @ApiModelProperty(name="materialBarcodeIdList",value = "扫描的条码id")
    private List<Long> materialBarcodeIdList;

    /**
     * 包箱/栈板码
     */
    @ApiModelProperty(name="cartonPalletCodes",value = "包箱/栈板码")
    private List<String> cartonPalletCodes;



}
