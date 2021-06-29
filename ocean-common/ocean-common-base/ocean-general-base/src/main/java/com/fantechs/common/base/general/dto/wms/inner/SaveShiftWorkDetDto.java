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
     * 库位条码集合
     */
    @ApiModelProperty(name="shiftWorkDetBarcodeDtos",value = "库位条码集合")
    List<SaveShiftWorkDetBarcodeDto> shiftWorkDetBarcodeDtos;
}
