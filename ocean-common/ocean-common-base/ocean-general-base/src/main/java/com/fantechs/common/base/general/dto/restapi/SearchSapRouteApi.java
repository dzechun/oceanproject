package com.fantechs.common.base.general.dto.restapi;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSapRouteApi implements Serializable {

    /**
     * 开始时间
     */
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    private String materialCode;

    /**
     * 结束时间
     */
    @ApiModelProperty(name="werks" ,value="工厂")
    private String werks;
}
