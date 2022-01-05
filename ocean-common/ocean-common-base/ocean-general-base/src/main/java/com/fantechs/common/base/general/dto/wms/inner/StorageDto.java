package com.fantechs.common.base.general.dto.wms.inner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StorageDto implements Serializable {

    /**
     * 库位ID
     */
    @ApiModelProperty(name="storageId",value = "库位ID")
    private Long storageId;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 库位类型，1-移出库位、2-移入库位
     */
    @ApiModelProperty(value = "type",example = "库位类型，1-移出库位、2-移入库位")
    private String type;

    /**
     * 物料集合
     */
    @ApiModelProperty(value = "list",example = "物料集合，type=1时有值")
    private List<InStorageMaterialDto> list;
}
