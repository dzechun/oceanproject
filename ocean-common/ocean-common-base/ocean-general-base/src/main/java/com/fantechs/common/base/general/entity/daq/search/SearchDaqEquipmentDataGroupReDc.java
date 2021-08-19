package com.fantechs.common.base.general.entity.daq.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchDaqEquipmentDataGroupReDc extends BaseQuery implements Serializable {

    /**
     * 设备组ID
     */
    @ApiModelProperty(name="equipmentDataGroupId",value = "设备组ID")
    private Long equipmentDataGroupId;

}
