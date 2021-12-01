package com.fantechs.common.base.general.entity.jinan.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRfidArea extends BaseQuery implements Serializable {
    /**
     * 区域编码
     */
    @ApiModelProperty(name="areaCode",value = "区域编码")
    private String areaCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(name="areaName",value = "区域名称")
    private String areaName;

    /**
     * 区域描述
     */
    @ApiModelProperty(name="areaDesc",value = "区域描述")
    private String areaDesc;
}
