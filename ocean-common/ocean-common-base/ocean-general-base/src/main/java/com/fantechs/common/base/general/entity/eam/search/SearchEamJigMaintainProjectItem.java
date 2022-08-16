package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigMaintainProjectItem extends BaseQuery implements Serializable {

    /**
     * 治具保养项目ID
     */
    @ApiModelProperty(name="jigMaintainProjectId",value = "治具保养项目ID")
    private Long jigMaintainProjectId;

}
