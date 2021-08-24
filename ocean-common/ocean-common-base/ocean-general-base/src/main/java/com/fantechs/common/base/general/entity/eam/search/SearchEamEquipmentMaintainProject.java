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
}
