package com.fantechs.common.base.general.dto.restapi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapBadnessCategoryApi implements Serializable {

    @ApiModelProperty(name="catalogue" ,value="目录")
    private String catalogue;

    @ApiModelProperty(name="badnessCodes" ,value="代码组")
    private String badnessCodes;
}
