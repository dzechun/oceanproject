package com.fantechs.common.base.general.entity.basic.search;


import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SearchBaseWarning extends BaseQuery {

    /**
     * 预警ID
     */
    @ApiModelProperty(name="warningId",value = "预警ID")
    private Long warningId;

}
