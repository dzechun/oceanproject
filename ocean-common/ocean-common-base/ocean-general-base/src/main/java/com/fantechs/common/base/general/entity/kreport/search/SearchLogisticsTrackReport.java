package com.fantechs.common.base.general.entity.kreport.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchLogisticsTrackReport extends BaseQuery implements Serializable{

    /**
     * 仓库ID
     */
    @ApiModelProperty(name="warehouseId",value = "仓库ID")
    private Long warehouseId;

    /**
     * 类型（1，收货  2，发货）
     */
    @ApiModelProperty(name="type",value = "类型（1，收货  2，发货）")
    private Byte type;


}
