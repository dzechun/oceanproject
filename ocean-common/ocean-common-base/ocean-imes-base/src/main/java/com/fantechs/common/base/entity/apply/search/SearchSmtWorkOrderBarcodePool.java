package com.fantechs.common.base.entity.apply.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtWorkOrderBarcodePool extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 469300618938581909L;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;
}