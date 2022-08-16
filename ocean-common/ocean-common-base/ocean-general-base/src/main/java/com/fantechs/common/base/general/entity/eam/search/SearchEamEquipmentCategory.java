package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentCategory extends BaseQuery implements Serializable {

    /**
     * 设备类别编码
     */
    @ApiModelProperty(name="equipmentCategoryCode",value = "设备类别编码")
    private String equipmentCategoryCode;

    /**
     * 设备类别描述
     */
    @ApiModelProperty(name="equipmentCategoryDesc",value = "设备类别描述")
    private String equipmentCategoryDesc;
}
