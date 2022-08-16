package com.fantechs.common.base.general.dto.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.lei
 * @Date 2021/10/15
 */
@Data
public class PrintBaseStorageCode {
    @ApiModelProperty(name = "printName",value = "打印机名称")
    private String printName;

    @ApiModelProperty(name = "printMode",value = "打印模版")
    private String printMode;

    @ApiModelProperty(name = "storageCode",value = "库位编码")
    private String storageCode;

    @ApiModelProperty(name = "size",value = "打印数量")
    private Integer size;
}
