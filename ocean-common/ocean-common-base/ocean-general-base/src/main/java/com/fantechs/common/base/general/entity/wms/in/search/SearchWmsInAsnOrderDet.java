package com.fantechs.common.base.general.entity.wms.in.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWmsInAsnOrderDet extends BaseQuery implements Serializable {
    /**
     * ANS单ID
     */
    @ApiModelProperty("Id")
    private Long asnOrderId;

    private Long asnOrderDetId;
}
