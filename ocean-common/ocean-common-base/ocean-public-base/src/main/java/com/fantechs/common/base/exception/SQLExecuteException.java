package com.fantechs.common.base.exception;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import lombok.Data;

/**
 * @Auther: bingo.ren
 * @Date: 2020/8/6 17:28
 * @Description: 数据库执行异常类
 * @Version: 1.0
 */
@Data
public class SQLExecuteException extends Exception {
    private Integer code;
    private String msg;
    public SQLExecuteException() {
        super();
    }

    public SQLExecuteException(ErrorCodeEnum errorCode, String msg){
        this.code=errorCode.getCode();
        this.msg=msg;
    }

    public SQLExecuteException(ErrorCodeEnum errorCode) {
        this.code=errorCode.getCode();
        this.msg=errorCode.getMsg();
    }
}
