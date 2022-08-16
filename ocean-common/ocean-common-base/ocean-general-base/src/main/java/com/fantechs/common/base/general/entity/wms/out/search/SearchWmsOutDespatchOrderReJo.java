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
public class SearchWmsOutDespatchOrderReJo extends BaseQuery implements Serializable {
    @ApiModelProperty("id")
    private Long despatchOrderId;
}
