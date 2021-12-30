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
     * 移入库位ID
     */
    @ApiModelProperty(name="inStorageId",value = "移入库位ID")
    private Long inStorageId;

    /**
     * 移出库位ID
     */
    @ApiModelProperty(name="outStorageId",value = "移出库位ID")
    private Long outStorageId;

    /**
     * 物料ID
     */
    @ApiModelProperty(name="materialId",value = "物料ID")
    private Long materialId;

    /**
     * 移位数量
     */
    @ApiModelProperty(name="materialQty",value = "移位数量")
    private BigDecimal materialQty;

    List<PDAWmsInnerDirectTransferOrderDetDto> pdaWmsInnerDirectTransferOrderDetDtos;

}
