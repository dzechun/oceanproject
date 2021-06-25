package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/23
 */
@Data
public class SearchOmOtherOutOrder extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "otherOutOrderCode",value = "订单号")
    private String otherOutOrderCode;

    @ApiModelProperty(name = "customerOrderCode",value = "客户单号")
    private String customerOrderCode;

    @ApiModelProperty(name = "orderStatus",value = "单据状态")
    private Byte orderStatus;
}
