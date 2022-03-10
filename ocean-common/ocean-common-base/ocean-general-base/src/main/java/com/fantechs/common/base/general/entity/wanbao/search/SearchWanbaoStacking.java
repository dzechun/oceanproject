package com.fantechs.common.base.general.entity.wanbao.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWanbaoStacking extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "proName",value = "产线名称")
    private String proName;

    @ApiModelProperty(name="stackingCode",value = "堆垛编码")
    private String stackingCode;

    @ApiModelProperty(name="stackingName",value = "堆垛名称")
    private String stackingName;

    @ApiModelProperty(name="usageStatus",value = "使用状态(1-空闲 2-使用中)")
    private Byte usageStatus;
}
