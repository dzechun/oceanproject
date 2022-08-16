package com.fantechs.common.base.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DicUtils {
    /**
     *  提供对外的 数据字典工具类
     * @param URL
     * @param DataCode
     * @return
     */
    public static Map<String,String> getMapFromBaseData(String URL,String DataCode){
        Map<String, String> map = new HashMap<>();
        String s = RestTemplateUtil.getForString(URL + "?dataCode=" + DataCode);
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            Map tempMap = (Map) jsonArray.get(i);
            map.put(tempMap.get("key").toString(),tempMap.get("value").toString() );
        }
        return map;
    }
}
