package com.fantechs.provider.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

@Data
public class ResponseEntityDTO {

    @ApiModelProperty(name = "code",value = "单据",example = "")
    private String code;
    @ApiModelProperty(name = "message",value = "接口返回信息",example = "保存成功")
    private String message;
    @ApiModelProperty(name = "data",value = "数据",example = "{}")
    private T data;
    @ApiModelProperty(name = "success",value = "接口返回代码, s: 正常; e: 错误; f: 失败",example = "s")
    private String success;
}
