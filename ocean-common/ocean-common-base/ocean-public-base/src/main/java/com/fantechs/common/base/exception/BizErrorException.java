package com.fantechs.common.base.exception;

import com.fantechs.common.constants.ErrorCodeEnum;
import lombok.Data;

/**
 * @Auther: bingo.ren
 * @Date: 2020/8/21 17:10
 * @Description: 业务处理异常类
 * @Version: 1.0
 */
@Data
public class BizErrorException extends Exception{
    private Integer code;
    private String msg;
    public BizErrorException() {
        super();
    }

    public BizErrorException(ErrorCodeEnum errorCode, String msg){
        this.code=errorCode.getCode();
        this.msg=msg;
    }

    public BizErrorException(ErrorCodeEnum errorCode) {
        this.code=errorCode.getCode();
        this.msg=errorCode.getMsg();
    }
}
