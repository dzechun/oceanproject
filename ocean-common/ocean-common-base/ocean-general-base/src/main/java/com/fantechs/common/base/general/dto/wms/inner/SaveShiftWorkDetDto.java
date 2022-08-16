package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SaveShiftWorkDetDto implements Serializable {

    /**
     * 移位单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "移位单ID,有则必填")
    private Long jobOrderId;

    /**
     * 移位单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "移位单明细ID,有则必填")
    private Long jobOrderDetId;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

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
     * 是否pda创建移位单
     */
    @ApiModelProperty(name="isPda",value = "是否pda创建移位单")
    private Boolean isPda;

    /**
     * 移位数量
     */
    @ApiModelProperty(name="materialQty",value = "移位数量")
    private BigDecimal materialQty;

    /**
     * 条码集合
     */
    @ApiModelProperty(name="barcodes",value = "条码集合")
    private List<String> barcodes;
}
