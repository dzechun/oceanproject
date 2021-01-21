package com.fantechs.common.base.agv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RcsResponseDTO {

    @ApiModelProperty(name = "code", value = "返回码", example = "0")
    private String code;

    @ApiModelProperty(name = "message", value = "返回信息", example = "返回信息")
    private String message;

    @ApiModelProperty(name = "reqCode", value = "请求编号", example = "请求编号")
    private String reqCode;

    @ApiModelProperty(name = "data", value = "自定义返回", example = "自定义返回")
    private String data;
}
