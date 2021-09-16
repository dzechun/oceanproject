package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SearchEamEquipmentBackupReEqu extends BaseQuery implements Serializable {
    /**
     * 设备id
     */
    @ApiModelProperty(name="equipmentId",value = "设备id")
    private String equipmentId;

    /**
     * 状态
     */
    @ApiModelProperty(name="status",value = "状态")
    private String status;

}
