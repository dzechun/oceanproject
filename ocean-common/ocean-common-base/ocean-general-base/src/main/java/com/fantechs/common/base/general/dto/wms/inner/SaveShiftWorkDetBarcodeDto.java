package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveShiftWorkDetBarcodeDto implements Serializable {

    /**
     * 上架单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "上架单明细ID")
    private Long jobOrderDetId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    private Long storageId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;
}
