package com.fantechs.common.base.general.entity.srm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSrmPoExpediteRecordReFile extends BaseQuery implements Serializable {


    /**
     * 订单跟催记录ID
     */
    @ApiModelProperty(name="poExpediteRecordId",value = "订单跟催记录ID")
    private Long poExpediteRecordId;

    private static final long serialVersionUID = 1L;
}
