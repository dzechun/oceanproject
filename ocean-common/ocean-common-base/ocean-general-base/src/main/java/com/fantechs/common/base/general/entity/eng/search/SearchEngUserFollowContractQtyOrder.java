package com.fantechs.common.base.general.entity.eng.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEngUserFollowContractQtyOrder extends BaseQuery implements Serializable {

    /**
     * 合同量单ID
     */
    @ApiModelProperty(name = "contractQtyOrderId",value = "合同量单ID")
    private Long contractQtyOrderId;

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId",value = "用户ID")
    private Long userId;
}
