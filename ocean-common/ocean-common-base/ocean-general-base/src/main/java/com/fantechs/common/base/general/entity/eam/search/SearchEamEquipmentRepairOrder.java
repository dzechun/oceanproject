package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquipmentRepairOrder extends BaseQuery implements Serializable {

    /**
     * 设备维修单id
     */
    @ApiModelProperty(name="equipmentRepairOrderId",value = "设备维修单id")
    private Long equipmentRepairOrderId;

    /**
     * 设备维修单号
     */
    @ApiModelProperty(name="equipmentRepairOrderCode",value = "设备维修单号")
    private String equipmentRepairOrderCode;

    /**
     * 设备条码
     */
    @ApiModelProperty(name="equipmentBarcode",value = "设备条码")
    private String equipmentBarcode;

    /**
     * 设备编码
     */
    @ApiModelProperty(name="equipmentCode",value = "设备编码")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty(name="equipmentName",value = "设备名称")
    private String equipmentName;

    /**
     * 设备型号
     */
    @ApiModelProperty(name="equipmentModel",value = "设备型号")
    private String equipmentModel;

    /**
     * 单据状态(1-待维修 2-维修中 3-已维修)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待维修 2-维修中 3-已维修)")
    private Byte orderStatus;

    /**
     * 设备条码ID
     */
    @ApiModelProperty(name="equipmentBarcodeId",value = "设备条码ID")
    private Long equipmentBarcodeId;

}
