package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapReportWorkApi implements Serializable {


    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    private String workOrderCode;

    @ApiModelProperty(name="processCode" ,value="工序")
    private String processCode;

    @ApiModelProperty(name="productionQty" ,value="产量")
    private String productionQty;

    @ApiModelProperty(name="reWorkQty" ,value="返工数量")
    private String reWorkQty;

    @ApiModelProperty(name="scrapQty" ,value="报废数量")
    private String scrapQty;


}
