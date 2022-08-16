package com.fantechs.common.base.general.entity.ews.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.Lei
 * @create 2021/3/8
 */
@Data
public class SearchEwsProcessScheduling extends BaseQuery implements Serializable {
    /**
     * 排程id
     */
    @ApiModelProperty(name="processSchedulingId",value = "排程id")
    private Long processSchedulingId;

    /**
     * 排程名称
     */
    @ApiModelProperty(name="processSchedulingName",value = "排程名称")
    private String processSchedulingName;
}
