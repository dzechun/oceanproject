package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/6/23
 */
@Data
public class SearchOmOtherOutOrderDet extends BaseQuery implements Serializable {
    @ApiModelProperty(name = "otherOutOrderId",value = "其他出库订单id")
    private Long otherOutOrderId;

    @ApiModelProperty(name = "otherOutOrderIdList",value = "其他出库订单id列表")
    private List<Long> otherOutOrderIdList;

    @ApiModelProperty(name="ifAllIssued",value = "是否已全部下发(0-否 1-是)")
    private Integer ifAllIssued;
}
