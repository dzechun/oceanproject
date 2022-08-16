package com.fantechs.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.lei
 * @Date 2021/10/14
 */
@Data
public class PrintModelDto {
    @ApiModelProperty(name = "printName",value = "打印机名称")
    private String printName;

    @ApiModelProperty(name = "printMode",value = "打印模版")
    private String printMode;

    @ApiModelProperty(name = "materialCode",value = "材料编码")
    private String materialCode;

    @ApiModelProperty(name = "materialName",value = "材料名称")
    private String materialName;

    @ApiModelProperty(name = "deviceCode",value = "装置号")
    private String deviceCode;

    @ApiModelProperty(name = "size",value = "打印数量")
    private Integer size;
}
