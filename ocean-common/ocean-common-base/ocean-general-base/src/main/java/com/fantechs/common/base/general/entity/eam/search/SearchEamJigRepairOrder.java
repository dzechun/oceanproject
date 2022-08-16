package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigRepairOrder extends BaseQuery implements Serializable {

    /**
     * 维修单id
     */
    @ApiModelProperty(name="jigRepairOrderId",value = "维修单id")
    private Long jigRepairOrderId;

    /**
     * 维修单号
     */
    @ApiModelProperty(name="jigRepairOrderCode",value = "维修单号")
    private String jigRepairOrderCode;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具条码")
    private String jigBarcode;

    /**
     * 治具编码
     */
    @ApiModelProperty(name="jigCode",value = "治具编码")
    private String jigCode;

    /**
     * 治具名称
     */
    @ApiModelProperty(name="jigName",value = "治具名称")
    private String jigName;

    /**
     * 治具型号
     */
    @ApiModelProperty(name="jigModel",value = "治具型号")
    private String jigModel;

    /**
     * 单据状态(1-待维修 2-维修中 3-已维修)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待维修 2-维修中 3-已维修)")
    private Byte orderStatus;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    private Long jigBarcodeId;
}
