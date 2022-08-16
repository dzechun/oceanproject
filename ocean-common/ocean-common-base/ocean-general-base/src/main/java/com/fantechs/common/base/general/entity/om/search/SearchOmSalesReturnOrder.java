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
public class SearchOmSalesReturnOrder extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "salesReturnOrderCode",value = "销退单号")
    private String salesReturnOrderCode;
    @ApiModelProperty(name = "materialOwnerName",value = "货主")
    private String materialOwnerName;
    @ApiModelProperty(name = "orderStatus",value = "单据状态")
    private Byte orderStatus;

    @ApiModelProperty(name = "completeDate",value = "要求完成日期")
    private String completeDate;
}
