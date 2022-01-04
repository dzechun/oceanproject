package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BatchSiftWorkDto implements Serializable {

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "materialCode",example = "物料编码")
    private String materialCode;
}
