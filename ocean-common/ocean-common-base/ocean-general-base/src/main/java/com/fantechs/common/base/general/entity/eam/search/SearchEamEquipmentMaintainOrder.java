package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentMaintainOrder extends BaseQuery implements Serializable {

    /**
     * 设备保养单ID
     */
    @ApiModelProperty(name="equipmentMaintainOrderId",value = "设备保养单ID")
    private Long equipmentMaintainOrderId;

    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    private Long equipmentBarcodeId;

    /**
     * 单据状态(1-待保养 2-已保养)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待保养 2-已保养)")
    private Byte orderStatus;
}
