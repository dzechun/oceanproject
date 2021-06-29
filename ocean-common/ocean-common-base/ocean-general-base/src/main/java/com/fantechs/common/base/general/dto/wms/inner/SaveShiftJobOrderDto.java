package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveShiftJobOrderDto implements Serializable {

    /**
     * 移入库位条码
     */
    @ApiModelProperty(name="storageCode",value = "移入库位条码")
    private String storageCode;

    /**
     * 移位单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "移位单明细ID")
    private Long jobOrderDetId;

    /**
     * 上架单ID
     */
    @ApiModelProperty(name="jobOrderId",value = "上架单ID,有则必填")
    private Long jobOrderId;
}
