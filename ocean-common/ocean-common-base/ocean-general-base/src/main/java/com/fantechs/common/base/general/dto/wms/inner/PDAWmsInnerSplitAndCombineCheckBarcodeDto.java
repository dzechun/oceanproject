package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PDAWmsInnerSplitAndCombineCheckBarcodeDto implements Serializable {

    /**
     * 类型（1-包箱 2-栈板）
     */
    @ApiModelProperty(name="type",value = "类型（1-包箱 2-栈板）")
    private Byte type;

    /**
     * 条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)
     */
    @ApiModelProperty(name="barcodeType",value = "条码类型(1-SN码 2-彩盒号 3-箱号 4-栈板号)")
    private Byte barcodeType;

    /**
     * 库位类型（1-存货 2-收货 3-发货）
     */
    @ApiModelProperty(name="storageType",value = "库位类型（1-存货 2-收货 3-发货）")
    private Byte storageType;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 包箱/栈板下级条码
     */
    @ApiModelProperty(name="nextLevelInventoryDetDtos",value = "包箱/栈板下级条码")
    private List<WmsInnerInventoryDetDto> nextLevelInventoryDetDtos;


}
