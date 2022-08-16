package com.fantechs.common.base.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020/4/21 11:01
 * @Description:
 * @Version:1.0
 */
public  class StringUtils {

    public static Boolean isEmpty(Object...str){
        for (int i = 0; i < str.length; i++) {
            if(objEmpty(str[i])){
                return true;
            }
        }
        return false;
    }
    public static Boolean isNotEmpty(Object...str){
        return !isEmpty(str);
    }
    //判空
    private static boolean objEmpty(Object obj){
        if (obj == null)
            return true;
        if(obj instanceof String)
            return (obj.equals("null")||obj.equals("NULL")||obj.equals("Null")||obj.equals(""));
        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;
        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();
        if (obj instanceof Map)
            return ((Map) obj).isEmpty();
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
}
