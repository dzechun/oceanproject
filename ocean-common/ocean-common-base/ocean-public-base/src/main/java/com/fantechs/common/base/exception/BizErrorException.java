package com.fantechs.common.base.exception;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: bingo.ren
 * @Date: 2020/8/21 17:10
 * @Description: 业务处理异常类
 * @Version: 1.0
 */
@Data
@Slf4j
public class BizErrorException extends RuntimeException{

    private static final long serialVersionUID = 3160241586346324994L;

    private Integer code;
    private String msg;
    public BizErrorException() {
        super();
    }
    public BizErrorException(Throwable cause) {
        super(cause);
    }

    public BizErrorException(String message) {
        super(message);
        this.code=ErrorCodeEnum.OPT20012002.getCode();
    }

    public BizErrorException(String message, Throwable cause) {
        super(message, cause);
    }


    public BizErrorException(int code, String msg){
        super(msg);
        this.code = code;
    }

    public BizErrorException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }
    public BizErrorException(ErrorCodeEnum codeEnum, Object... args) {
        super(String.format(codeEnum.getMsg(), args));
        this.code = codeEnum.getCode();
    }
}
