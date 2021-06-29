package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveShiftWorkDetDto implements Serializable {

    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID,有则必填")
    private Long jobOrderId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID,有则必填")
    private Long warehouseId;

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
     * 条码集合
     */
    @ApiModelProperty(name="barcodes",value = "条码集合")
    private List<String> barcodes;
}
