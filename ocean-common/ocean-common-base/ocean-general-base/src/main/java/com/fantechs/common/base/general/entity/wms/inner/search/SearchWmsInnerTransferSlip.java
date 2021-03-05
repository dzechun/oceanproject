package com.fantechs.common.base.general.entity.wms.inner.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SearchWmsInnerTransferSlip extends BaseQuery {

    /**
     * 调拨单号
     */
    @ApiModelProperty(name="transferSlipCode",value = "调拨单号")
    private String transferSlipCode;

    /**
     * 处理人
     */
    @ApiModelProperty(name="processorUserId",value = "处理人")
    private Long processorUserId;

    /**
     * 修改人ID
     */
    @ApiModelProperty(name="modifiedUserId",value = "修改人ID")
    private Long modifiedUserId;
}
