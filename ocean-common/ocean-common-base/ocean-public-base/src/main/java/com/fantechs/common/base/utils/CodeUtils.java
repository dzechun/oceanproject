package com.fantechs.common.base.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CodeUtils {

    @Autowired
    private static RedisTemplate<String, Object> redisTemplate;

    public CodeUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public static String getId(String startStr) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date date=new Date();
        String formatDate=sdf.format(date);
        String key="key"+startStr+formatDate;
        Long incr = getIncr(key, getCurrent2TodayEndMillisTime());
        if(incr==0) {
            incr = getIncr(key, getCurrent2TodayEndMillisTime());//从001开始
        }
        DecimalFormat df=new DecimalFormat("000");//三位序列号
        return startStr+formatDate+df.format(incr);
    }


    public static String getScheduleNo(String orderNo) {
        String key="key"+orderNo;
        Long incr = getIncr(key, getCurrent2TodayEndMillisTime());
        if(incr==0) {
            incr = getIncr(key, getCurrent2TodayEndMillisTime());//从001开始
        }
        DecimalFormat df=new DecimalFormat("-0000");//四位序列号
        return orderNo+df.format(incr);
    }

    public static Long getIncr2(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        if ((null == increment || increment.longValue() == 0) && 365 > 0) {//初始设置过期时间
            entityIdCounter.expire(365, TimeUnit.DAYS);//单位天
        }
        return increment;
    }



    public static Long getIncr(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.MILLISECONDS);//单位毫秒
        }
        return increment;
    }

    //现在到今天结束的毫秒数
    public static Long getCurrent2TodayEndMillisTime() {
        Calendar todayEnd = Calendar.getInstance();
        // Calendar.HOUR 12小时制
        // HOUR_OF_DAY 24小时制
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis()-System.currentTimeMillis();
    }


    /**
     * 根据编码规则生成编码
     * @param codeRule
     * @return
     */
    public static  String patternCode(String codeRule){
        String code=null;
        if(StringUtils.isNotEmpty(codeRule)){
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile("\\[(.*?)]");
            Matcher matcher = pattern.matcher(codeRule);
            while (matcher.find()){
                String re = matcher.group(0);
                String s =matcher.group(1);
                sb.append(s);
                char[] rules = s.toCharArray();
                for(char q :rules){
                    //String ruleType = getTypeCode(q+"");
                }

            }
        }

        return code;
    }


    public static  String getTypeCode(String str,String customizeValue){
        String ruleType=null;
        Map<String, Object> map= new HashMap<>();
        Calendar cal = Calendar.getInstance();
        if(StringUtils.isNotEmpty(customizeValue)){
            JSONArray jsonArray = JSONArray.fromObject(customizeValue);
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject obj = JSONObject.fromObject(jsonArray.get(i));
                Iterator it = obj.keys();
                while (it.hasNext()) {
                    String key = String.valueOf(it.next());
                    String value = (String) obj.get(key);
                    map.put(key, value);
                }
            }
            //map= JsonUtils.jsonToMap(customizeValue);
        }
        switch(str){
            //月
            case "[M]" :
                ruleType =new SimpleDateFormat("MM").format(new Date());
                break;
            //周
            case "[W]" :
                //周固定2位
                Format format=new DecimalFormat("00");
                ruleType =  format.format(cal.get(Calendar.WEEK_OF_YEAR));
                break;
            //日
            case "[D]" :
                ruleType =new SimpleDateFormat("dd").format(new Date());
                break;
            //周的日
            case "[K]" :
                ruleType =  cal.get(Calendar.DAY_OF_WEEK)+"";
                break;
            //年的日
            case "[A]" :
                //年的日固定3位
                Format decimalFormat=new DecimalFormat("000");
                ruleType =  decimalFormat.format(cal.get(Calendar.DAY_OF_YEAR));
                break;
            //自定义年
            case "[y]" :
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                String year = sdf.format(new Date());
                for(String key : map.keySet()){
                    if(key.equals(year)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            //自定义月
            case "[m]" :
                int m = cal.get(Calendar.MONTH) + 1;
                String month = String.valueOf(m);
                for(String key : map.keySet()){
                    if(key.equals(month)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            //自定义日
            case "[d]" :
                int d = cal.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(d);
                for(String key : map.keySet()){
                    if(key.equals(day)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            //自定义周
            case "[w]" :
                int w = cal.get(Calendar.WEEK_OF_YEAR);
                String week = String.valueOf(w);
                for(String key : map.keySet()){
                    if(key.equals(week)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            default :
        }
        return  ruleType;
    }

    public static  String getTypeCode(String str,String customizeValue,String planYear,String planMonth,String planDay){
        String ruleType=null;
        Map<String, Object> map= new HashMap<>();
        Calendar cal = Calendar.getInstance();
        if(StringUtils.isNotEmpty(customizeValue)){
            JSONArray jsonArray = JSONArray.fromObject(customizeValue);
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject obj = JSONObject.fromObject(jsonArray.get(i));
                Iterator it = obj.keys();
                while (it.hasNext()) {
                    String key = String.valueOf(it.next());
                    String value = (String) obj.get(key);
                    map.put(key, value);
                }
            }
            //map= JsonUtils.jsonToMap(customizeValue);
        }
        switch(str){
            //月
            case "[M]" :
                ruleType =new SimpleDateFormat("MM").format(new Date());
                break;
            //周
            case "[W]" :
                //周固定2位
                Format format=new DecimalFormat("00");
                ruleType =  format.format(cal.get(Calendar.WEEK_OF_YEAR));
                break;
            //日
            case "[D]" :
                ruleType =new SimpleDateFormat("dd").format(new Date());
                break;
            //周的日
            case "[K]" :
                ruleType =  cal.get(Calendar.DAY_OF_WEEK)+"";
                break;
            //年的日
            case "[A]" :
                //年的日固定3位
                Format decimalFormat=new DecimalFormat("000");
                ruleType =  decimalFormat.format(cal.get(Calendar.DAY_OF_YEAR));
                break;
            //自定义年
            case "[y]" :
                String year=null;
                if(StringUtils.isEmpty(planYear)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                    year = sdf.format(new Date());
                }
                else {
                    year=planYear;
                }
                for(String key : map.keySet()){
                    if(key.equals(year)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            //自定义月
            case "[m]" :
                String month=null;
                if(StringUtils.isEmpty(planMonth)) {
                    int m = cal.get(Calendar.MONTH) + 1;
                    month = String.valueOf(m);
                }
                else{
                    month=planMonth;
                }
                for(String key : map.keySet()){
                    if(key.equals(month)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            //自定义日
            case "[d]" :
                String day=null;
                if(StringUtils.isEmpty(planDay)) {
                    int d = cal.get(Calendar.DAY_OF_MONTH);
                    day = String.valueOf(d);
                }
                else {
                    day=planDay;
                }
                for(String key : map.keySet()){
                    if(key.equals(day)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            //自定义周
            case "[w]" :
                int w = cal.get(Calendar.WEEK_OF_YEAR);
                String week = String.valueOf(w);
                for(String key : map.keySet()){
                    if(key.equals(week)){
                        ruleType = (String) map.get(key);
                    }
                }
                break;
            default :
        }
        return  ruleType;
    }


    /**
     *
     * @param str1  当前最大流水号
     * @param str2  步长
     * @param code  自定义流水号规则
     * @return
     */
    public static String generateSerialNumber(String str1, String str2, String code) {
        Character[] nums= ArrayUtils.toObject(code.toCharArray());
        List<Character> list = Arrays.asList(nums);
        int length = code.length();
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int i = s1.length - 1;
        int j = s2.length - 1;
        int temp = 0;// 进位
        StringBuilder sb = new StringBuilder();
        while (i >= 0 && j >= 0) {
            char c1 = s1[i];
            char c2 = s2[j];
            int index1 = list.indexOf(c1);
            int index2 = list.indexOf(c2);
            int sum = index1 + index2 + temp;
            if (sum >= length) {
                temp = 1;
                sb.append(list.get(sum % length));
            } else {
                temp=0;
                sb.append(list.get(sum));
            }
            i--;
            j--;
        }
        while (i >= 0) {
            int sum = list.indexOf(s1[i]) + temp;
            if (sum >=length) {
                temp = 1;
                sb.append(list.get(sum % length));
            } else {
                temp=0;
                sb.append(list.get(sum));
            }
            i--;
        }
        while (j >= 0) {
            int sum = list.indexOf(s2[j]) + temp;
            if (sum >=length) {
                temp = 1;
                sb.append(list.get(sum % length));
            } else {
                temp=0;
                sb.append(list.get(sum));
            }
            j--;
        }
        if(temp!=0){
            sb.append('1');
        }
        return sb.reverse().toString();
    }

}
