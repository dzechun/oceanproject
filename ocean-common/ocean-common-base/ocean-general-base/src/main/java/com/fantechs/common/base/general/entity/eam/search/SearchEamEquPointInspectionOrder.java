package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamEquPointInspectionOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "equipmentCategoryId",value = "设备分类ID")
    private Long equipmentCategoryId;

    @ApiModelProperty(name = "orderStatus",value = "单据状态(1-待点检 2-已点检)")
    private Byte orderStatus;

    @ApiModelProperty(name = "equPointInspectionOrderCode",value = "点检单号")
    private String equPointInspectionOrderCode;

    @ApiModelProperty(name = "equipmentBarcode",value = "设备条码")
    private String equipmentBarcode;

    @ApiModelProperty(name = "equipmentCode",value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    private String equipmentName;

}
