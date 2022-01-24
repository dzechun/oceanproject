package com.fantechs.common.base.general.entity.wanbao.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchWanbaoReadHead extends BaseQuery implements Serializable {

    @ApiModelProperty(name = "platformName",value = "月台名称")
    private String platformName;

    @ApiModelProperty(name="readHeadName",value = "读头名称")
    private String readHeadName;

    @ApiModelProperty(name="readHeadIp",value = "读头IP")
    private String readHeadIp;
}
