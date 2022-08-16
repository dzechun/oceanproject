package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMesSfcKeyPartRelevance extends BaseQuery implements Serializable {

    @ApiModelProperty(name="keyPartRelevanceId",value = "关键部件关联id")
    private Long keyPartRelevanceId;

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="workOrderBarcodeId",value = "产品条码ID")
    private Long workOrderBarcodeId;

    @ApiModelProperty(name="processId",value = "工序ID")
    private Long processId;

    @ApiModelProperty(name="materialId",value = "产品物料ID")
    private Long materialId;

    @ApiModelProperty(name="stationId",value = "工位ID")
    private Long stationId;

    @ApiModelProperty(name="partBarcode",value = "部件条码")
    private String partBarcode;

    @ApiModelProperty(name="barcodeCode",value = "产品条码")
    private String barcodeCode;

}
