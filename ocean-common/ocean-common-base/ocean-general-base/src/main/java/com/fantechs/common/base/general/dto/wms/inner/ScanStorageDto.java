package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ScanStorageDto implements Serializable {

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 条码
     */
    @ApiModelProperty(value = "barcode",example = "条码")
    private String barcode;

    /**
     * 库位类型，1-移出库位、2-移入库位
     */
    @ApiModelProperty(value = "type",example = "库位类型，1-移出库位、2-移入库位")
    private String type;
}
