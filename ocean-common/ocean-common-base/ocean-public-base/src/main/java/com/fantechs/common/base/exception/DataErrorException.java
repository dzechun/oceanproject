package com.fantechs.common.base.exception;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import lombok.Data;

/**
 * @Auther: bingo.ren
 * @Date: 2020/8/6 14:47
 * @Description: 数据错误异常类
 * @Version: 1.0
 */
@Data
public class DataErrorException extends Exception {
    private Integer code;
    private String msg;
    public DataErrorException() {
        super();
    }

    public DataErrorException(ErrorCodeEnum errorCode, String msg){
        this.code=errorCode.getCode();
        this.msg=msg;
    }

    public DataErrorException(ErrorCodeEnum errorCode) {
        this.code=errorCode.getCode();
        this.msg=errorCode.getMsg();
    }
}
