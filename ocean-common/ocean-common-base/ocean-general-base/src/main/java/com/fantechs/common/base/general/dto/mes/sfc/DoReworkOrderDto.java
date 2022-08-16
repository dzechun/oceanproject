package com.fantechs.common.base.general.dto.mes.sfc;

import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DoReworkOrderDto implements Serializable {

    @ApiModelProperty(name = "searchMesSfcBarcodeProcess", value = "返工单搜索条件对象")
    private SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess;

    @ApiModelProperty(name = "keyPartRelevanceDtoList", value = "需清除部件列表")
    private List<MesSfcKeyPartRelevanceDto> keyPartRelevanceDtoList;

    @ApiModelProperty(name = "reworkOrderCode", value = "返工单号")
    private String reworkOrderCode;

    @ApiModelProperty(name = "routeId", value = "工艺路线ID")
    private Long routeId;

    @ApiModelProperty(name = "processId", value = "工序ID")
    private Long processId;

    @ApiModelProperty(name = "clearCarton", value = "清除包箱")
    private Boolean clearCarton;

    @ApiModelProperty(name = "clearColorBox", value = "清除彩盒")
    private Boolean clearColorBox;

    @ApiModelProperty(name = "clearPallet", value = "清除栈板")
    private Boolean clearPallet;
}
