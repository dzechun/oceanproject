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
}
