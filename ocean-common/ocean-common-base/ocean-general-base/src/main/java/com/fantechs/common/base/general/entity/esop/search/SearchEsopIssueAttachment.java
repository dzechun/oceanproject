package com.fantechs.common.base.general.entity.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEsopIssueAttachment extends BaseQuery implements Serializable {

    /**
     * 问题ID
     */
    @ApiModelProperty(name="issueId",value = "问题ID")
    private Long issueId;

}
