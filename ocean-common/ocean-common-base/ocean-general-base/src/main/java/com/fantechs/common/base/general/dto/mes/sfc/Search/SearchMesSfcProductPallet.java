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
public class SearchMesSfcProductPallet extends BaseQuery implements Serializable {

    @ApiModelProperty(name="workOrderId",value = "工单ID")
    private Long workOrderId;

    @ApiModelProperty(name="materialId",value = "产品物料ID")
    private Long materialId;

    @ApiModelProperty(name="stationId",value = "工位ID")
    private Long stationId;

    @ApiModelProperty(name="palletCode",value = "栈板号")
    private String palletCode;

    @ApiModelProperty(name="closeStatus",value = "关闭状态(0-未关闭 1-已关闭)")
    private Byte closeStatus;
}
