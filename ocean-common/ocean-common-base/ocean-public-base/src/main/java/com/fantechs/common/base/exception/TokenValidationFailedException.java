package com.fantechs.common.base.exception;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import lombok.Data;

/**
 * @Auther: bingo.ren
 * @Date: 2020/6/20 10:34
 * @Description: token异常
 * @Version: 1.0
 */
@Data
public class TokenValidationFailedException extends Exception {
	private Integer code;
	private String msg;
	public TokenValidationFailedException() {
		super();
	}

	public TokenValidationFailedException(ErrorCodeEnum errorCode, String msg){
		this.code=errorCode.getCode();
		this.msg=msg;
	}

	public TokenValidationFailedException(ErrorCodeEnum errorCode) {
		this.code=errorCode.getCode();
		this.msg=errorCode.getMsg();
	}
}
