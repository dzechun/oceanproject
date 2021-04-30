package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWmsInAsnOrderDet extends BaseQuery implements Serializable {
    /**
     * ANS单ID
     */
    @ApiModelProperty("Id")
    private Long asnOrderId;
}
