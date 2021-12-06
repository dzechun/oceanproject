package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsIncomingInspectionOrderDet extends BaseQuery implements Serializable {
    /**
     * 来料检验单id
     */
    @ApiModelProperty(name = "incomingInspectionOrderId", value = "来料检验单id")
    private Long incomingInspectionOrderId;
}
