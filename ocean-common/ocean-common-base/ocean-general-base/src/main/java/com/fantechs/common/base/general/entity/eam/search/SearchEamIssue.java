package com.fantechs.common.base.general.entity.eam.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEamIssue extends BaseQuery implements Serializable {

    /**
     * 产品编码
     */
    @ApiModelProperty(name="materialCode",value = "产品编码")
    private String materialCode;


}
