package com.fantechs.common.base.general.entity.basic.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Mr.Lei
 * @create 2021/3/10
 */
@Data
public class QuartzSearch implements Serializable {
    @ApiModelProperty(value = "被执行完整接口",example = "被执行完整接口")
    private String url;
    @ApiModelProperty(value = "调用方法（1、GET 2、POST）",example = "1")
    private String method;
    @ApiModelProperty(value = "传递参数",example = "Map<String,Object>")
    private Map<String,Object> map;
}
