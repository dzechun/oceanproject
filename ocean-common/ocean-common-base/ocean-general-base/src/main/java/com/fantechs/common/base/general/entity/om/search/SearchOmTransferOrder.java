package com.fantechs.common.base.general.entity.om.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/6/15
 */
@Data
public class SearchOmTransferOrder extends BaseQuery implements Serializable {
    @ApiModelProperty("调拨单号")
    private String transferOrderCode;
    @ApiModelProperty("相关单号")
    private String relatedOrderCode;
    @ApiModelProperty("调出仓库")
    private String outWarehouseName;
    @ApiModelProperty("调入仓库")
    private String inWarehouseName;
    @ApiModelProperty("单据状态")
    private String orderStatus;
}
