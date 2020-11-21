package com.fantechs.common.base.entity.apply.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtWorkOrderCardCollocation extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 5055978634595108144L;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;
}
