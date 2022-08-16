package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentParam extends BaseQuery implements Serializable {


    /**
     * 设备类别名称
     */
    @ApiModelProperty(name="equipmentCategoryName",value = "设备类别名称")
    private String equipmentCategoryName;


}
