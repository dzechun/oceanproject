package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchEngUserFollowContractQtyOrder extends BaseQuery implements Serializable {

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name = "contractQtyOrderId",value = "合同量单ID")
    private Long contractQtyOrderId;

    /**
     * 合同量单ID列表
     */
    @ApiModelProperty(name = "contractQtyOrderIds",value = "合同量单ID列表")
    private List<Long> contractQtyOrderIds;

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId",value = "用户ID")
    private Long userId;
}
