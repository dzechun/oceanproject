package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SaveShiftJobOrderDto implements Serializable {

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    private Long storageId;

    /**
     * 移位单明细ID
     */
    @ApiModelProperty(name="jobOrderDetId",value = "移位单明细ID")
    private Long jobOrderDetId;
}
