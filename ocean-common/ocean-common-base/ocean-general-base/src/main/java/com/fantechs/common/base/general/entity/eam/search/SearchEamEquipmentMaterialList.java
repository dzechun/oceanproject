package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentMaterialList extends BaseQuery implements Serializable {

    /**
     * 设备绑定产品表头ID
     */
    @ApiModelProperty(name="equipmentMaterialId",value = "设备绑定产品表头ID")
    private Long equipmentMaterialId;


}
