package com.fantechs.common.base.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author sunny
 * @date 2019/11/12
 */
@ApiModel(value = "数据返回实体类",
        description =
                "所有的接口返回的接口均由ResponseEntity实体封装后返回,主要字段有code,message,data。" +
                        "如果数据正常返回,code为0,失败则为非0;错误信息在message字段中给出;如果请求正常返回,数据在data字段中获取"
)
public class ResponseEntity<T>   implements Serializable {

    @ApiModelProperty(name = "code",value = "状态码,0为业务正常返回,否则为异常返回",example = "0")
    private int code;
    @ApiModelProperty(name = "message",value = "提示信息",example = "登录失败")
    private String message;
    @ApiModelProperty(name = "data",value = "数据",example = "{}")
    private T data;
    @ApiModelProperty(name = "count",value = "总数",example = "{}")
    private int count;


    public ResponseEntity() {
        this.code = 0;
        this.message = "";
        this.data=null;
        this.count=0;
    }

    public ResponseEntity(Builder<T> builder) {
        this.code = builder.status;
        this.message = builder.message;
        this.data = builder.data;
        this.count = builder.count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setErrorMessage(String msg){
        this.code = 1;
        this.message = msg;
    }

    public void setErrorMessage(String msg,int code){
        this.code = code;
        this.message = msg;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\"code\":\"");
        builder.append(code).append("\",\"message\":\"").append(message)
                .append("\",\"data\":\"");
        if (data != null) {
            builder.append(data.toString());
        }
        builder.append("\"}");
        return builder.toString();
    }

    public static class Builder<T>{
        private int status;
        private String message;
        private T data;
        private int count;

        public Builder() {
            this.status= 0;
            this.message = "";
        }

        public Builder<T> setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> setData(T data) {
            this.data = data;
            return this;
        }
        public Builder<T> setCount(int count) {
            this.count = count;
            return this;
        }


        public ResponseEntity<T> build(){
            return new ResponseEntity<>(this);
        }
    }
}
