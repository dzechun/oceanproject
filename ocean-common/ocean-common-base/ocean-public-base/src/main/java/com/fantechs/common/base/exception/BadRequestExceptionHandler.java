package com.fantechs.common.base.exception;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by lfz on 2020/10/10.
 */
@RestControllerAdvice
@Slf4j
public class BadRequestExceptionHandler {
    /**
     *  校验错误拦截处理
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationBodyException(MethodArgumentNotValidException exception){

        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();

            errors.forEach(p ->{
                FieldError fieldError = (FieldError) p;
                log.error("数据验证不通过 : object{"+fieldError.getObjectName()+"},field{"+fieldError.getField()+
                        "},errorMessage{"+fieldError.getDefaultMessage()+"}");

            });
            return ControllerUtil.returnFail(errors.get(0).getDefaultMessage(),ErrorCodeEnum.GL99990100.getCode());

        }
         return ControllerUtil.returnFailByParameError();
    }


    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity parameterTypeException(HttpMessageConversionException exception){

        log.error(exception.getCause().getLocalizedMessage());
        return ControllerUtil.returnFailByParameError();

    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(ConstraintViolationException exception){
        log.error(exception.getMessage());
        return ControllerUtil.returnFail("类型转换错误:"+exception.getMessage(),ErrorCodeEnum.GL99990100.getCode());

    }

    @ExceptionHandler(value = BizErrorException.class)
    public ResponseEntity bizErrorException(BizErrorException e){
        log.error(getExceptionInfo(e));
        return ControllerUtil.returnFail(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(value = SQLExecuteException.class)
    public ResponseEntity sqlExecuteException(SQLExecuteException e){
        log.error(getExceptionInfo(e));
        return ControllerUtil.returnFail("SQL执行错误:"+e.getMsg(), e.getCode());
    }

    @ExceptionHandler(value = DataErrorException.class)
    public ResponseEntity dataErrorException(DataErrorException e){
        log.error(getExceptionInfo(e));
        return ControllerUtil.returnFail("类型转换错误:"+e.getMessage(), e.getCode());
    }

    @ExceptionHandler(value = TokenValidationFailedException.class)
    public ResponseEntity dataErrorException(TokenValidationFailedException e){
        log.error(getExceptionInfo(e));
        return ControllerUtil.returnFail("Token错误:"+e.getMessage(), e.getCode());
    }

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity exception(Exception e){
//        log.error(getExceptionInfo(e));
//        return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
//    }


    private static String getExceptionInfo(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        ex.printStackTrace(printStream);
        String rs = new String(out.toByteArray());
        try {
            printStream.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}
