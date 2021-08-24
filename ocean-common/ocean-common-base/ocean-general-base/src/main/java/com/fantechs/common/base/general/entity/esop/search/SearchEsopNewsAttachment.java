package com.fantechs.common.base.general.entity.esop.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEsopNewsAttachment extends BaseQuery implements Serializable {

    /**
     * 新闻id
     */
    @ApiModelProperty(name="newsId",value = "新闻id")
    private Long newsId;


}
