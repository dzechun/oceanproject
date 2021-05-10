package com.fantechs.common.base.general.entity.wms.out.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Data
public class SearchWmsOutDespatchOrder extends BaseQuery implements Serializable {

    @ApiModelProperty("装车单号")
    private String despatchOrderCode;
    @ApiModelProperty("柜号")
    private String containerNumber;
    @ApiModelProperty("封条号")
    private String sealNumber;
}
