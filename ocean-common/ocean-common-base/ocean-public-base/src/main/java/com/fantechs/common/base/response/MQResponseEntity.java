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
public class MQResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = -3313878706348433292L;

    @ApiModelProperty(name = "code", value = "执行动作编码", example = "0")
    private int code;
    @ApiModelProperty(name = "message", value = "提示信息", example = "登录失败")
    private String message;
    @ApiModelProperty(name = "data", value = "数据", example = "{}")
    private T data;
    @ApiModelProperty(name = "count", value = "总数", example = "{}")
    private int count;
//    @ApiModelProperty(name = "snedTime",value = "发送时间",example = "{}")
//    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
//    private Date snedTime;
    @ApiModelProperty(name = "key", value = "身份标识", example = "身份标识")
    private String key;


    public MQResponseEntity() {
        this.code = 0;
        this.message = "";
        this.data = null;
        this.count = 0;
//        this.snedTime=new Date();
        this.key = "";
    }

    public MQResponseEntity(Builder<T> builder) {
        this.code = builder.status;
        this.message = builder.message;
        this.data = builder.data;
        this.count = builder.count;
//        this.snedTime = builder.snedTime;
        this.key = builder.key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setErrorMessage(String msg) {
        this.code = 1;
        this.message = msg;
    }

    public void setErrorMessage(String msg, int code) {
        this.code = code;
        this.message = msg;
    }

//    public Date getSnedTime() {
//        return snedTime;
//    }
//
//    public void setSnedTime(Date snedTime) {
//        this.snedTime = snedTime;
//    }

    @Override
    public String toString() {
        return "MQResponseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", count=" + count +
//                ", snedTime=" + snedTime +
                ", key='" + key + '\'' +
                '}';
    }

    public static class Builder<T> {
        private int status;
        private String message;
        private T data;
        private int count;
        //        private Date snedTime;
        private String key;

        public Builder() {
            this.status = 0;
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

//        public Builder<T> snedTime(Date snedTime) {
//            this.snedTime = snedTime;
//            return this;
//        }
//        public Builder<T> snedKey(String key) {
//            this.key = key;
//            return this;
//        }


        public MQResponseEntity<T> build() {
            return new MQResponseEntity<>(this);
        }
    }
}
