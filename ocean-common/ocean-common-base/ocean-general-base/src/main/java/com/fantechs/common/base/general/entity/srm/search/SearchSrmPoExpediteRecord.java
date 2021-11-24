package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import com.fantechs.common.base.support.ValidGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchSrmPoExpediteRecord extends BaseQuery implements Serializable {


    /**
     * 订单跟催ID
     */
    @ApiModelProperty(name="poExpediteId",value = "订单跟催ID")
    private Long poExpediteId;


    private static final long serialVersionUID = 1L;
}
