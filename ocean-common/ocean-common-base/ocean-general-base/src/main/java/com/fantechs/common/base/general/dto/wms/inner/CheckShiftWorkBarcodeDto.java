package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class CheckShiftWorkBarcodeDto implements Serializable {


    /**
     * 移出库位ID
     */
    @ApiModelProperty(name="storageId",value = "移出库位ID")
    private Long storageId;

    /**
     * 条码
     */
    @ApiModelProperty(name="barcode",value = "条码")
    private String barcode;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID,有则必填")
    private Long warehouseId;

    /**
     * 移位单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "移位单明细ID,有则必填")
    private Long jobOrderDetId;
}
