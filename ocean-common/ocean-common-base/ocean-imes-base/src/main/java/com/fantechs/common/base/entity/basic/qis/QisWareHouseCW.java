package com.fantechs.common.base.entity.basic.qis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QisWareHouseCW {

    @ApiModelProperty(value = "仓库编码")
    private String ckNo;

    @ApiModelProperty(value = "仓库名称")
    private String ckName;

    @ApiModelProperty(value = "储位编码")
    private String code;

    @ApiModelProperty(value = "储位名称")
    private String name;

    @ApiModelProperty(value = "组织名称")
    private String orgname;

    @ApiModelProperty(value = "组织ID")
    private String orgnum;
}
