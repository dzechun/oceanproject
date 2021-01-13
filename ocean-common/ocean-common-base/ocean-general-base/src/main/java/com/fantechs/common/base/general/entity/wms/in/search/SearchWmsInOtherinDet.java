package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInOtherinDet extends BaseQuery implements Serializable {

    /**
     * 其他入库单ID
     */
    @ApiModelProperty(name="otherinId",value = "其他入库单ID")
    private String otherinId;
}
