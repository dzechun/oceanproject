package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcRepairOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "repairOrderCode", value = "维修单号")
    private String repairOrderCode;

    @ApiModelProperty(name = "orderStatus", value = "单据状态(1、待维修 2、已维修)")
    private Byte orderStatus;

    @ApiModelProperty(name = "workOrderCode", value = "工单号")
    private String workOrderCode;
}
