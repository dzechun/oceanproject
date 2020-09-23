package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtStorage extends BaseQuery implements Serializable {

    /**
     * 储位编码
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageCode;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageName;

    /**
     * 储位描述
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private String storageDesc;

    /**
     * 仓库ID
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private Long warehouseId;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name = "storageCode",value = "储位编码")
    private Long warehouseAreaId;
}
