package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchBaseStorageTaskPoint extends BaseQuery implements Serializable {

    /**
     * 配送点编码
     */
    @ApiModelProperty(name="taskPointCode",value = "配送点编码")
    private String taskPointCode;

    /**
     * 配送点名称
     */
    @ApiModelProperty(name="taskPointName",value = "配送点名称")
    private String taskPointName;

    /**
     * 配送点类型(1-备料 2-存料)
     */
    @ApiModelProperty(name="taskPointType",value = "配送点类型(1-备料 2-存料)")
    private Byte taskPointType;

    /**
     * 仓库编码
     */
    @ApiModelProperty(name="warehouseCode",value = "仓库编码")
    private String warehouseCode;

    /**
     * 库区编码
     */
    @ApiModelProperty(name="warehouseAreaCode",value = "库区编码")
    private String warehouseAreaCode;

    /**
     * 库位编码
     */
    @ApiModelProperty(name="storageCode",value = "库位编码")
    private String storageCode;

    /**
     * 库位配送点状态(1-空闲 2-使用)
     */
    @ApiModelProperty(name="storageTaskPointStatus",value = "库位配送点状态(1-空闲 2-使用)")
    private Byte storageTaskPointStatus;
}