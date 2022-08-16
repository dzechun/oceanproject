package com.fantechs.common.base.general.dto.mes.sfc.Search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SearchMesSfcReworkOrder extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "reworkOrderCode", value = "返工单号")
    private String reworkOrderCode;

    @ApiModelProperty(name="reworkStatus",value = "返工状态(1-未返工 2-返工中 3-返工完成 4-已撤消)")
    private Byte reworkStatus;


}
