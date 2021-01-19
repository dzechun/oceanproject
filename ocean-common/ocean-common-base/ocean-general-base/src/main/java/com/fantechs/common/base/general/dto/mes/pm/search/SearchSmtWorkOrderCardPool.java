package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSmtWorkOrderCardPool extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -8345474494489351304L;

    /**
     * 工单ID
     */
    @ApiModelProperty(name="workOrderId" ,value="工单ID")
    private Long workOrderId;
    /**
     * 工单流转卡编码
     */
    @ApiModelProperty(name="workOrderCardId" ,value="工单流转卡编码")
    private String workOrderCardId;
}
