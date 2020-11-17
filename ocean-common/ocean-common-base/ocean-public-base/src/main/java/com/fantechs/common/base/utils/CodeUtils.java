package com.fantechs.common.base.utils;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        return todayEnd.getTimeInMillis()-new Date().getTime();
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
                    String ruleType = getTypeCode(q+"");
                }

            }
        }

        return code;
    }


    public static  String getTypeCode(String str){
        String ruleType=null;
        Calendar cal = Calendar.getInstance();
        switch(str){
            //年
            case "[Y]" :
                ruleType = DateUtils.getDateString(new Date(),"yyyyMMdd");
                break;
            //月
            case "[M]" :
                ruleType =  cal.get(Calendar.MONTH) + 1+"";
                break;
            //周
            case "[W]" :
                ruleType =  cal.get(Calendar.WEEK_OF_YEAR)+"";
                break;
            //日
            case "[D]" :
                ruleType =  cal.get(Calendar.DATE)+"";
                break;
            //周的日
            case "[K]" :
                ruleType =  cal.get(Calendar.DAY_OF_WEEK)+"";
                break;
            //年的日
            case "[A]" :
                ruleType =  cal.get(Calendar.DAY_OF_YEAR)+"";
                break;
            default :
        }
        return  ruleType;
    }

   /* public static void main(String[] args) {
        String ss = "smt[YMDW][yymd]???";
        System.out.println(ss.split("Y").length-1 );
       // patternCode(ss);
    }*/

    public static void main(String[] args) {
        //String code="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String code="0123456789ABCDEFGHJKLMNPRSTUVWXYZ";
        String str1 = "0HJ";
        int num=16;
        Character[] nums= ArrayUtils.toObject(code.toCharArray());
        List<Character> list = Arrays.asList(nums);
        String str2 = list.get(num).toString();
        String r=generateSerialNumber(str1,str2,code);
        System.out.println(r);

        String a="[K]";
        String typeCode = getTypeCode(a);
        System.out.println(typeCode);

        Calendar cal= Calendar.getInstance();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week);
    }


    /**
     *
     * @param str1  当前最大流水号
     * @param str2  步长
     * @param code  自定义流水号
     * @return
     */
    public static String generateSerialNumber(String str1, String str2, String code) {
        //Character[] nums = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
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
