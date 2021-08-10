package com.fantechs.common.base.general.entity.eam.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class SearchEamEquipmentDataGroup extends BaseQuery implements Serializable {

    /**
     * 设备组ID
     */
    @ApiModelProperty(name="equipmentDataGroupId",value = "设备组ID")
    private Long equipmentDataGroupId;
}
