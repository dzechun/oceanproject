package com.fantechs.common.base.general.entity.basic.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SearchBasePlatform extends BaseQuery implements Serializable {

    /**
     * 月台编码
     */
    @ApiModelProperty(name="platformCode",value = "月台编码")
    private String platformCode;

    /**
     * 月台名称
     */
    @ApiModelProperty(name="platformName",value = "月台名称")
    private String platformName;

}
