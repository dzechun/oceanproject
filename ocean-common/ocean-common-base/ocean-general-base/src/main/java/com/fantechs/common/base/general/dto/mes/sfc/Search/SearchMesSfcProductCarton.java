package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcProductCarton extends BaseQuery implements Serializable {

    @ApiModelProperty(name="materialId",value = "产品物料ID")
    private Long materialId;

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="cartonCode",value = "包箱号")
    private String cartonCode;

    @ApiModelProperty(name="stationId",value = "工位ID")
    private Long stationId;

    @ApiModelProperty(name="closeStatus",value = "关箱状态(0-未关闭 1-已关闭)")
    private Byte closeStatus;
}
