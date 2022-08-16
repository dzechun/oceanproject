package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/21
 */
@Data
public class SearchOmSalesReturnOrderDet extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "salesReturnOrderId",value = "id")
    private Long salesReturnOrderId;
}
