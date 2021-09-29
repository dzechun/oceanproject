package com.fantechs.entity.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lzw
 * @Date 2021/9/26
 */
@Data
public class SearchInventoryAgeDetUreport extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "storageId",value = "库位id")
    private Long storageId;

    @ApiModelProperty(name = "materialId",value = "物料")
    private Long materialId;

    @ApiModelProperty(name = "inventoryStatusId",value = "库存状态id")
    private Long inventoryStatusId;

    @ApiModelProperty(name = "rangeStart",value = "库龄范围起始值")
    private Integer rangeStart;

    @ApiModelProperty(name = "rangeEnd",value = "库龄范围结束值")
    private Integer rangeEnd;

}
