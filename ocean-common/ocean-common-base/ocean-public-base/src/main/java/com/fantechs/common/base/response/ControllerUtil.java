package com.fantechs.common.base.response;


import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.SpringUtil;
import com.fantechs.common.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 用于操作controller公共方法的工具类
 */
public class ControllerUtil {
    private static Logger log= LoggerFactory.getLogger(ControllerUtil.class);

    private static RedisUtil redisUtil=(RedisUtil) SpringUtil.getBean("redisUtil");

    /***
     * 统一返回成功的DTO
     */
    public static <T> ResponseEntity<T> returnSuccess(){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setCode(0);
        return  dto;
    }

    /***
     * 统一返回成功的DTO 带数据
     */
    public static <T> ResponseEntity<T> returnSuccess(String message,T data){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(message);
        dto.setData(data);
        dto.setCode(0);
        return  dto;
    }

    /***
     * 统一返回成功的DTO 带数据
     */
    public static <T> ResponseEntity<T> returnSuccess(String message,T data,int count){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(message);
        dto.setData(data);
        dto.setCount(count);
        dto.setCode(0);
        return  dto;
    }
    /***
     * 统一返回成功的DTO 不带数据
     */
    public static <T> ResponseEntity<T> returnSuccess(String message){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(message);
        dto.setCode(0);
        return  dto;
    }
    /***
     * 统一返回成功的DTO 带数据 没有消息
     */
    public static <T> ResponseEntity<T> returnDataSuccess(T data,int count){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setData(data);
        dto.setCount(count);
        dto.setCode(0);
        return  dto;
    }

    /***
     * 统一返回成功的DTO 带数据 消息，不带条数
     */
    public static <T> ResponseEntity<T> returnDataSuccess(String message ,T data){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setData(data);
        dto.setMessage(message);
        dto.setCode(0);
        return  dto;
    }

    /**
     * 返回错误信息、错误码
     * @param message
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> returnFail(String message, int errorCode){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(message);
        dto.setCode(errorCode);
        return  dto;
    }

    /**
     * 返回错误信息对象
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> returnFail(ErrorCodeEnum errorCode){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(errorCode.getMsg());
        dto.setCode(errorCode.getCode());
        return  dto;
    }

    /**
     * 封装动态查询条件Map
     * @param o 以键值对的形式传参
     * @return
     */
    public static Map<String,Object> dynamicCondition(Object...o){
        Map<String,Object> hashMap = new HashMap<>();
        for (int i = 0; i < o.length; i=i+2) {
            if(!StringUtils.isEmpty(o[i+1])){
                hashMap.put(o[i].toString(),o[i+1].toString());
                log.info("生成动态条件：{}:{}" ,o[i].toString(),o[i+1].toString() );
            }
        }
        return hashMap;
    }


    /**
     * 查询对象转Map
     * @param o
     * @return
     */
    public static Map<String,Object> dynamicConditionByEntity(Object o){
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = o.getClass();
        List<Class<?> > List = new LinkedList<>();
        Class<?> suCl = o.getClass().getSuperclass();
        if(StringUtils.isNotEmpty(suCl)){
            List.add(suCl);
        }
        List.add(clazz);
        for(Class<?> clazz1 : List){
            for (Field field : clazz1.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = null;
                try {
                    Object o1 = field.get(o);
                    value = o1==null?null:o1;
                    if(escapeExprSpecialWord(value)){
                        throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"非法字符");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(value != null){
                    map.put(fieldName, value);
                }
            }
        }

        return map;
    }


    /**
     * 对于部分增删改查统一一个返回数据
     * @param count
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> returnCRUD(int count){
        if(count>0){
            return returnSuccess("操作成功");
        }else{
            return returnFail(ErrorCodeEnum.GL99990005.getMsg(), ErrorCodeEnum.GL99990005.getCode());
        }
    }

    public static <T> ResponseEntity<T> returnFailByParameError(){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(ErrorCodeEnum.GL99990100.getMsg());
        dto.setCode(ErrorCodeEnum.GL99990100.getCode());
        return  dto;
    }

    public static <T> ResponseEntity<T> returnFailByParameNull(){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(ErrorCodeEnum.GL99990100.getMsg());
        dto.setCode(ErrorCodeEnum.GL99990100.getCode());
        return  dto;
    }
    public static <T> ResponseEntity<T> returnFailByDataError(){
        ResponseEntity<T> dto=new ResponseEntity<>();
        dto.setMessage(ErrorCodeEnum.GL99990500.getMsg());
        dto.setCode(ErrorCodeEnum.GL99990500.getCode());
        return  dto;
    }


    public  static Boolean escapeExprSpecialWord(Object keyword) {
        boolean flag = false;
        if (StringUtils.isNotEmpty(keyword)) {
            Object specialWord = redisUtil.get("specialWord");
            if (StringUtils.isNotEmpty(specialWord)) {

                SysSpecItem sysSpecItem = JSONObject.parseObject(JSONObject.toJSONString(specialWord), SysSpecItem.class);

                String[] fbsArr = sysSpecItem.getParaValue().split(",");
                for (String key : fbsArr) {
                    if (StringUtils.isEmpty(keyword)) {
                        continue;
                    }
                    if (keyword.toString().contains(key)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }
}
