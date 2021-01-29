package com.fantechs.common.base.general.dto.mes.pm.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/1/29
 */
@Data
public class SearchMesPmProcessListCard extends BaseQuery implements Serializable {
    @ApiModelProperty(value = "工单流程卡编码",example = "工单流程卡编码")
    private String workOrderCardId;

    @ApiModelProperty(value = "工单id",example = "工单id")
    private String workOrderId;
}
