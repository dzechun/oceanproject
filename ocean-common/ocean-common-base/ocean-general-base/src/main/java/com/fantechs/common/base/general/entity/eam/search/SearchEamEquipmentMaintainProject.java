package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentMaintainProject extends BaseQuery implements Serializable {

    /**
     * 设备类别id
     */
    @ApiModelProperty(name="equipmentCategoryId",value = "设备类别id")
    private Long equipmentCategoryId;

    @ApiModelProperty(name = "equipmentMaintainProjectCode",value = "设备保养项目编码")
    private String equipmentMaintainProjectCode;

    @ApiModelProperty(name = "equipmentMaintainProjectName",value = "设备保养项目名称")
    private String equipmentMaintainProjectName;

    @ApiModelProperty(name = "equipmentCategoryName",value = "设备类别名称")
    private String equipmentCategoryName;
}
