package com.fantechs.common.base.general.entity.kreport.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWarehouseKanban  extends BaseQuery implements Serializable{

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 产品编码
     */
    @ApiModelProperty(name="codes",value = "产品编码")
    private String codes;

    /**
     * 是否按照天查询(1、是 0、否)
     */
    @ApiModelProperty(name="isDay",value = "是否按照天查询(1、是 0、否)")
    private Byte isDay;

}
