package com.fantechs.common.base.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: bingo.ren
 * @Date: 2020/6/17 17:44
 * @Description: controller返回实体
 * @Version: 1.0
 */
@ApiModel(
        value = "数据返回实体类",
        description = "所有的接口返回的接口均由ResponseEntity实体封装后返回" +
                "主要字段有code、message、data、count；" +
                "如果数据正常，返回code为0，失败则为非0；" +
                "返回信息在message字段中给出；" +
                "如果请求正常返回，数据在data字段中获取；" +
                "count为返回数据数量，或总量"
)
@Data
public class ResponseDTO<T>   implements Serializable {

    @ApiModelProperty(name = "code",value = "状态码,0为业务正常返回,否则为异常返回",example = "0")
    private String code;
    @ApiModelProperty(name = "message",value = "提示信息",example = "登录失败")
    private String message;
    @ApiModelProperty(name = "data",value = "数据",example = "{}")
    private T data;
    @ApiModelProperty(name = "count",value = "总数",example = "{}")
    private int count;

    public ResponseDTO() {
        this.code = "0";
        this.message = "操作成功";
        this.data=null;
        this.count=0;
    }
}
