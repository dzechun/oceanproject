package com.fantechs.provider.imes.apply.utils;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class BinaryConversionUtils {

    /**
     * 用递归来实现10转成其他进制
     *
     * @param iSrc
     * @return
     */
    public static String DeciamlToBaseConversion(int iSrc,String customizeValue) {
        String result = "";
        int key;
        int value;

        Character[] nums= ArrayUtils.toObject(customizeValue.toCharArray());
        List<Character> list = Arrays.asList(nums);

        key = iSrc / customizeValue.length();
        value = iSrc - key * customizeValue.length();
        if (key != 0) {
            result = result + DeciamlToBaseConversion(key,customizeValue);
        }

        result = result + list.get(value);

        return result;
    }

    public static void main(String[] args) {
        String customizeValue = "0123456789ABCDEFGHJKLMNPRSTUVWXYZ";
        String s = DeciamlToBaseConversion(10220,customizeValue);
        System.out.println(s);
    }

}
