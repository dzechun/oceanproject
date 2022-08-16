package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigPointInspectionProject extends BaseQuery implements Serializable {

    /**
     * 治具点检编码
     */
    @ApiModelProperty(name="jigPointInspectionProjectCode",value = "治具点检编码")
    private String jigPointInspectionProjectCode;

    /**
     * 治具点检名称
     */
    @ApiModelProperty(name="jigPointInspectionProjectName",value = "治具点检名称")
    private String jigPointInspectionProjectName;

    /**
     * 治具点检描述
     */
    @ApiModelProperty(name="jigPointInspectionProjectDesc",value = "治具点检描述")
    private String jigPointInspectionProjectDesc;

    /**
     * 治具类别id
     */
    @ApiModelProperty(name="jigCategoryId",value = "治具类别id")
    private Long jigCategoryId;

}
