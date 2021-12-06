package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsIncomingInspectionOrderDetSample extends BaseQuery implements Serializable {
    /**
     * 来料检验单明细id
     */
    @ApiModelProperty(name = "incomingInspectionOrderDetId", value = "来料检验单明细id")
    private Long incomingInspectionOrderDetId;
}
