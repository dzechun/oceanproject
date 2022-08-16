package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquPointInspectionProjectItem extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "equPointInspectionProjectId",value = "点检项目ID")
    private Long equPointInspectionProjectId;
}
