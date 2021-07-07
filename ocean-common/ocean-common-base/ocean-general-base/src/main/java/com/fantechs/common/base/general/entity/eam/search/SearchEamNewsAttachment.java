package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamNewsAttachment extends BaseQuery implements Serializable {

    /**
     * 新闻id
     */
    @ApiModelProperty(name="newsId",value = "新闻id")
    private Long newsId;


}
