package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamJigPointInspectionOrder extends BaseQuery implements Serializable {

    /**
     * 治具点检单ID
     */
    @ApiModelProperty(name="jigPointInspectionOrderId",value = "治具点检单ID")
    private Long jigPointInspectionOrderId;

    /**
     * 治具条码ID
     */
    @ApiModelProperty(name="jigBarcodeId",value = "治具条码ID")
    private Long jigBarcodeId;

    /**
     * 治具点检单号
     */
    @ApiModelProperty(name="jigPointInspectionOrderCode",value = "治具点检单号")
    private String jigPointInspectionOrderCode;

    /**
     * 治具条码
     */
    @ApiModelProperty(name="jigBarcode",value = "治具保养单号")
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
     * 治具类别
     */
    @ApiModelProperty(name="jigCategoryName",value = "治具类别")
    private String jigCategoryName;

    /**
     * 单据状态(1-待保养 2-已保养)
     */
    @ApiModelProperty(name="orderStatus",value = "单据状态(1-待保养 2-已保养)")
    private Byte orderStatus;


}
