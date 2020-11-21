package com.fantechs.provider.imes.apply.utils;

import java.util.HashMap;

public class BinaryConversionUtils {

    // 定义其他进制数字
    private static final String customizeValue = "0123456789ABCDEFGHJKLMNPRSTUVWXYZ";
    // 拿到10进制转换其他进制的值键对
    private static HashMap<Integer, Character> baseConversion = createMapBaseConversion();


    private static HashMap<Integer, Character> createMapBaseConversion() {
        HashMap<Integer, Character> map = new HashMap<>();
        for (int i = 0; i < customizeValue.length(); i++) {
            map.put(i, customizeValue.charAt(i));
        }
        return map;
    }

    /**
     * 用递归来实现10转成其他进制
     *
     * @param iSrc
     * @return
     */
    public static String DeciamlToThirtySix(int iSrc) {
        String result = "";
        int key;
        int value;

        key = iSrc / customizeValue.length();
        value = iSrc - key * customizeValue.length();
        if (key != 0) {
            result = result + DeciamlToThirtySix(key);
        }

        result = result + baseConversion.get(value).toString();

        return result;
    }

}
