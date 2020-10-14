package com.fantechs.common.base.entity.apply.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtWorkOrderBom extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -8427547885722759457L;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;

    /**
     * 零件物料ID
     */
    @ApiModelProperty(name="partMaterialId" ,value="工单ID")
    private Long partMaterialId;
}
