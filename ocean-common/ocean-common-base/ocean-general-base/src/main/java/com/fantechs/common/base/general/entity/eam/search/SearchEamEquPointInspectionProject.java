package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquPointInspectionProject extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "equipmentCategoryId",value = "设备分类ID")
    private Long equipmentCategoryId;

    @ApiModelProperty(name = "equPointInspectionProjectCode",value = "点检项目编码")
    private String equPointInspectionProjectCode;

    @ApiModelProperty(name = "equPointInspectionProjectName",value = "点检项目名称")
    private String equPointInspectionProjectName;

    @ApiModelProperty(name = "equPointInspectionProjectDesc",value = "点检项目描述")
    private String equPointInspectionProjectDesc;
}
