package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsIpqcInspectionOrderDet extends BaseQuery implements Serializable {

    /**
     * Ipqc检验单ID
     */
    @ApiModelProperty(name="ipqcInspectionOrderId",value = "Ipqc检验单ID")
    private Long ipqcInspectionOrderId;

}
