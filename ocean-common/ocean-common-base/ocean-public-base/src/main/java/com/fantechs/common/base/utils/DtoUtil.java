package com.fantechs.common.base.utils;


import com.fantechs.common.base.response.ResponseEntity;

/**
 * 用于返回Dto的工具类
 * Created by XX on 17-5-8.
 */
public class DtoUtil {

    public static String success = "true";

    public static String fail = "false";

    public static String errorCode = "0";

    /***
     * 统一返回成功的DTO
     */
    public static ResponseEntity returnSuccess() {
        ResponseEntity dto = new ResponseEntity();

        return dto;
    }

    /***
     * 统一返回成功的DTO 带数据
     */
    public static ResponseEntity returnSuccess(String message, Object data, int count) {
        ResponseEntity dto = new ResponseEntity();
        dto.setMessage(message);
        dto.setData(data);
        dto.setCount(count);
        return dto;
    }

    /***
     * 统一返回成功的DTO 不带数据
     */
    public static ResponseEntity returnSuccess(String message) {
        ResponseEntity dto = new ResponseEntity();
        dto.setMessage(message);
        return dto;
    }


    /***
     * 统一返回成功的DTO 带数据,不带message
     */
    public static ResponseEntity returnSuccess(Object data) {
        ResponseEntity dto = new ResponseEntity();
        dto.setMessage("");
        dto.setData(data);
        return dto;
    }

    /***
     * 统一返回成功的DTO 带数据
     */
    public static ResponseEntity returnSuccess(String message, Object data) {
        ResponseEntity dto = new ResponseEntity();
        dto.setMessage(message);
        dto.setData(data);
        return dto;
    }

    /***
     * 统一返回成功的DTO 带数据 没有消息
     */
    public static ResponseEntity returnDataSuccess(Object data, int count) {
        ResponseEntity dto = new ResponseEntity();
        dto.setData(data);
        dto.setCount(count);
        return dto;
    }

    public static ResponseEntity returnFail(String message, String errorCode) {
        ResponseEntity dto = new ResponseEntity();
        dto.setMessage(message);
        dto.setCode(Integer.parseInt(errorCode));
        return dto;
    }
}