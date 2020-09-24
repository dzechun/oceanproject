package com.fantechs.common.base.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtStorageMaterial extends BaseQuery implements Serializable {

    /**
     * 仓库ID
     */
    @ApiModelProperty(name = "warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 仓库区域ID
     */
    @ApiModelProperty(name = "warehouseAreaId",value = "仓库区域ID")
    private Long warehouseAreaId;

    /**
     * 储位ID
     */
    @ApiModelProperty(name = "storageId",value = "储位ID")
    private Long storageId;

    /**
     * 储位名称
     */
    @ApiModelProperty(name = "storageName",value = "储位编码")
    private String storageName;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "materialId",value = "物料ID")
    private Long materialId;

    /**
     * 物料描述
     */
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    private String materialDesc;
}
