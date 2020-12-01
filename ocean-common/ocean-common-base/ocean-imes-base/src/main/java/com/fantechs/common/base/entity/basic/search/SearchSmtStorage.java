package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtStorage extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -6508024716295865028L;
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

    /**
     * 储位ID
     */
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;
}
