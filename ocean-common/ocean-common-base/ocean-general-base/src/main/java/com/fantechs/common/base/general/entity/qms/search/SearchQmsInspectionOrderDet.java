package com.fantechs.common.base.general.entity.qms.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsInspectionOrderDet extends BaseQuery implements Serializable {

    /**
     * 检验单ID
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单ID")
    private Long inspectionOrderId;

    /**
     * 检验单明细ID
     */
    @ApiModelProperty(name="inspectionOrderDetId",value = "检验单明细ID")
    private Long inspectionOrderDetId;

}
