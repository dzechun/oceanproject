package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class SearchBaseInventoryStatus extends BaseQuery implements Serializable {

    /**
     * 仓库
     */
    @ApiModelProperty(name="warehouseId" ,value="仓库")
    private String warehouseId;

    /**
     * 货主
     */
    @ApiModelProperty(name="materialOwnerId" ,value="货主")
    private String materialOwnerId;

    /**
     * 状态名称
     */
    @ApiModelProperty(name="inventoryStatusName" ,value="状态名称")
    private String inventoryStatusName;

}
