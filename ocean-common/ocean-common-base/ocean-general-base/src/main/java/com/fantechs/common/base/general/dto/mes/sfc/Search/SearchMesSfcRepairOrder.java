package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchMesSfcRepairOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "repairOrderCode", value = "维修单号")
    private String repairOrderCode;


}
