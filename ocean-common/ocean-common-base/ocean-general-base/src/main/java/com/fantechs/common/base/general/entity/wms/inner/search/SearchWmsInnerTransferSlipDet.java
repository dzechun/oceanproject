package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SearchWmsInnerTransferSlipDet extends BaseQuery {

    /**
     * 调拨单ID
     */
    @ApiModelProperty(name="transferSlipId",value = "调拨单ID")
    private Long transferSlipId;
}
