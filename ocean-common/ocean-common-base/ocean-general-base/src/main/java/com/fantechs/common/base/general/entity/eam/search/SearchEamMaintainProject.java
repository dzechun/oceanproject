package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamMaintainProject extends BaseQuery implements Serializable {

    /**
     * 保养编码
     */
    @ApiModelProperty(name="maintainProjectCode",value = "保养编码")
    private String maintainProjectCode;

    /**
     * 保养名称
     */
    @ApiModelProperty(name="maintainProjectName",value = "保养名称")
    private String maintainProjectName;

}
