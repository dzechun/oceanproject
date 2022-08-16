package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigMaintainProject extends BaseQuery implements Serializable {

    /**
     * 治具保养编码
     */
    @ApiModelProperty(name="jigMaintainProjectCode",value = "治具保养编码")
    private String jigMaintainProjectCode;

    /**
     * 治具保养名称
     */
    @ApiModelProperty(name="jigMaintainProjectName",value = "治具保养名称")
    private String jigMaintainProjectName;

    /**
     * 治具保养描述
     */
    @ApiModelProperty(name="jigMaintainProjectDesc",value = "治具保养描述")
    private String jigMaintainProjectDesc;

    /**
     * 治具类别id
     */
    @ApiModelProperty(name="jigCategoryId",value = "治具类别id")
    private Long jigCategoryId;

}
