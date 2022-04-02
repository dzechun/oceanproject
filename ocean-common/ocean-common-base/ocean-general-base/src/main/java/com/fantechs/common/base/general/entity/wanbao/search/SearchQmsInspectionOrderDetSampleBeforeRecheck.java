package com.fantechs.common.base.general.entity.wanbao.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchQmsInspectionOrderDetSampleBeforeRecheck extends BaseQuery implements Serializable {

    /**
     * 检验单ID
     */
    @ApiModelProperty(name="inspectionOrderId",value = "检验单ID")
    private Long inspectionOrderId;


}
