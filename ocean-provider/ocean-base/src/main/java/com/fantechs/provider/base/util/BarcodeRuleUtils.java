package com.fantechs.provider.base.util;


import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBarcodeRuleSpecService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wcz
 */
@Component
@Slf4j
public class BarcodeRuleUtils {

    @Autowired
    private BaseBarcodeRuleSpecService baseBarcodeRuleSpecService;
    @Autowired
    private RedisUtil redisUtil;

    // 声明对象
    private static BarcodeRuleUtils barcodeRuleUtils;

    @PostConstruct // 初始化
    public void init(){
        barcodeRuleUtils = this;
        barcodeRuleUtils.baseBarcodeRuleSpecService = this.baseBarcodeRuleSpecService;
        barcodeRuleUtils.redisUtil = this.redisUtil;
    }

    /**
     *
     * @param list 条码规则配置
     * @param maxCode  已生成的最大流水号
     * @param code 产品料号、生产线别、客户料号
     * @param params 执行函数参数
     * @return
     */
    public static String analysisCode(List<BaseBarcodeRuleSpec> list, String maxCode, String code,String params,Map<String,Object> map){
        StringBuilder sb=new StringBuilder();
        Calendar cal= Calendar.getInstance();
        if(StringUtils.isNotEmpty(list)){
            for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
                //格式
                String specification = baseBarcodeRuleSpec.getSpecification();
                //长度
                Integer barcodeLength = baseBarcodeRuleSpec.getBarcodeLength();
                //步长
                Integer step = baseBarcodeRuleSpec.getStep();
                //自定义参数值
                String customizeValue = baseBarcodeRuleSpec.getCustomizeValue();
                //补位方向(0.前  1.后)
                Byte fillDirection = baseBarcodeRuleSpec.getFillDirection();
                //补位符
                String fillOperator = baseBarcodeRuleSpec.getFillOperator();
                //截取方向(0.前  1.后)
                Byte interceptDirection = baseBarcodeRuleSpec.getInterceptDirection();
                //截取位置
                Integer interceptPosition = baseBarcodeRuleSpec.getInterceptPosition();
                //初始值
                Integer initialValue = baseBarcodeRuleSpec.getInitialValue();
                //自定义函数名称
                String functionName = baseBarcodeRuleSpec.getCustomizeName();


                if("[G]".equals(specification)){
                     sb.append(customizeValue);
                }else if("[Y]".equals(specification)){
                    if(barcodeLength==1){
                        int value= cal.get(Calendar.YEAR);
                        String str = String.valueOf(value);
                        String year = str.substring(str.length() - 1);
                        sb.append(year);
                    }else if(barcodeLength==2){
                        SimpleDateFormat sdf=new SimpleDateFormat("yy");
                        String year = sdf.format(new Date());
                        sb.append(year);
                    }else {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                        String year = sdf.format(new Date());
                        sb.append(year);
                    }
                }else if("[P]".equals(specification)||"[L]".equals(specification)||"[C]".equals(specification)){
                    if(map!=null && !map.isEmpty()){
                        switch (specification){
                            case "[P]":
                                code = map.get("[P]").toString();
                                break;
                            case "[L]":
                                code = map.get("[L]").toString();
                                break;
                            case "[C]":
                                code = map.get("[C]").toString();
                                break;
                        }
                    }
                    //产品料号的长度
                    int length = code.length();
                    //长度不足需要补位
                    if(barcodeLength>length){
                        if(StringUtils.isNotEmpty(fillOperator)){
                             if(0==fillDirection){
                                 for (int i=0;i<barcodeLength-length;i++){
                                     sb.append(fillOperator);
                                 }
                                 sb.append(code);
                             }else {
                                 sb.append(code);
                                 for (int i=0;i<barcodeLength-length;i++){
                                     sb.append(fillOperator);
                                 }
                             }
                        }else {
                            throw new BizErrorException("产品料号/生产线别/客户料号的长度不够，不能没有补位符");
                        }
                        //长度超过需要截取
                    }else if(barcodeLength<length){
                         //截取位置从1开始
                         if(StringUtils.isNotEmpty(interceptPosition)){
                             if(0==interceptDirection){
                                 if(interceptPosition>=barcodeLength){
                                     code.substring(interceptPosition-barcodeLength,interceptPosition);
                                 }else {
                                     throw new BizErrorException("产品料号/生产线别/客户料号从该截取位置截取长度不够");
                                 }
                             }else {
                                 if(interceptDirection+barcodeLength-1<=length){
                                      code.substring(interceptPosition-1,interceptDirection+barcodeLength-1);
                                 }else {
                                     throw new BizErrorException("产品料号/生产线别/客户料号从该截取位置截取长度不够");
                                 }
                             }
                         }else {
                             throw new BizErrorException("产品料号/生产线别/客户料号需要截取是必须有截取位置");
                         }
                    }else {
                        sb.append(code);
                    }
                }else if("[S]".equals(specification)){
                    String customizeCode="0123456789";
                    maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeCode, String.valueOf(step));
                }else if("[F]".equals(specification)){
                    String customizeCode="0123456789ABCDEF";
                    maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeCode, getStep(step, customizeCode));
                }else if("[b]".equals(specification)){
                    customizeValue="0123456789ABCDEFGHJKLMNPQRSTVWXYZ";
                    maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeValue, getStep(step, customizeValue));
                }else if("[c]".equals(specification)) {
                    customizeValue = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeValue, getStep(step, customizeValue));
                }else if("[f]".equals(specification)){
                    //执行函数获取解析码


                    String param = barcodeRuleUtils.baseBarcodeRuleSpecService.executeFunction(functionName,params);
                    if(StringUtils.isEmpty(param)){
                        throw new BizErrorException("条码规则执行函数失败，请检查执行函数");
                    }
                    Map<String,Object> paramMap = JSON.parseObject(param,Map.class);
                    if(Integer.parseInt(paramMap.get("mesCode").toString())!=200){
                        throw new BizErrorException(paramMap.get("message").toString());
                    }else {
                        param = paramMap.get("param").toString();
                    }


                    if(param.length()<barcodeLength){
                        while (param.length()<barcodeLength){
                            StringBuffer s = new StringBuffer();
                            s.append(param).append("0");
                            param = s.toString();
                        }
                    }
                    if(param.length()>barcodeLength){
                        throw new BizErrorException("执行函数返回值超出设定长度");
                    }
                    sb.append(param);
                }else {  //月、周、日、周的日、年的日、自定义年、月、日、周
                    String typeCode = CodeUtils.getTypeCode(specification,customizeValue);
                    sb.append(typeCode);
                }
            }
        }

        return sb.toString();
    }

    /**
     *
     * @param list 条码规则配置
     * @param code 产品料号、生产线别、客户料号
     * @param params 执行函数参数
     * @param qty 生成数量
     * @param key redis key
     * @return
     */
    public static List<String> batchAnalysisCode(List<BaseBarcodeRuleSpec> list, String code,String params,Map<String,Object> map, Integer qty, String key,String planYear,String planMonth,String planDay){
        List<String> barcodeList = new ArrayList<>();

        Map<String, String> functionResultMap = new HashMap<>();

        long forSpecTime = System.currentTimeMillis();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            //自定义函数名称
            String functionName = baseBarcodeRuleSpec.getCustomizeName();
            if (StringUtils.isNotEmpty(functionName)){
                String param = barcodeRuleUtils.baseBarcodeRuleSpecService.executeFunction(functionName,params);
                functionResultMap.put(functionName, param);
            }
        }
        log.info("for执行函数循环 执行总时长 : {}毫秒)",(System.currentTimeMillis() - forSpecTime));

        long forqtyTime = System.currentTimeMillis();
        for (Integer item = 0; item < qty; item++) {
            String lastBarCode = null;
            boolean hasKey = barcodeRuleUtils.redisUtil.hasKey(key);
            if(hasKey){
                Object redisRuleData = barcodeRuleUtils.redisUtil.get(key);
                lastBarCode = String.valueOf(redisRuleData);
            }

            log.info("key:"+key+"============ lastBarCode:"+lastBarCode + "================");

            //获取最大流水号
            //String maxCode = getMaxSerialNumber(list, lastBarCode);
            String maxCode = getMaxSerialNumber(list, lastBarCode, planYear, planMonth, planDay);

            log.info("最大流水号1:"+maxCode+"============");

            StringBuilder sb=new StringBuilder();
            Calendar cal= Calendar.getInstance();
            if(StringUtils.isNotEmpty(list)){
                for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
                    //格式
                    String specification = baseBarcodeRuleSpec.getSpecification();
                    //长度
                    Integer barcodeLength = baseBarcodeRuleSpec.getBarcodeLength();
                    //步长
                    Integer step = baseBarcodeRuleSpec.getStep();
                    //自定义参数值
                    String customizeValue = baseBarcodeRuleSpec.getCustomizeValue();
                    //补位方向(0.前  1.后)
                    Byte fillDirection = baseBarcodeRuleSpec.getFillDirection();
                    //补位符
                    String fillOperator = baseBarcodeRuleSpec.getFillOperator();
                    //截取方向(0.前  1.后)
                    Byte interceptDirection = baseBarcodeRuleSpec.getInterceptDirection();
                    //截取位置
                    Integer interceptPosition = baseBarcodeRuleSpec.getInterceptPosition();
                    //初始值
                    Integer initialValue = baseBarcodeRuleSpec.getInitialValue();
                    //自定义函数名称
                    String functionName = baseBarcodeRuleSpec.getCustomizeName();


                    if("[G]".equals(specification)){
                        sb.append(customizeValue);
                    }else if("[Y]".equals(specification)){
                        if(barcodeLength==1){
                            int value= cal.get(Calendar.YEAR);
                            String str = String.valueOf(value);
                            String year = str.substring(str.length() - 1);
                            sb.append(year);
                        }else if(barcodeLength==2){
                            SimpleDateFormat sdf=new SimpleDateFormat("yy");
                            String year = sdf.format(new Date());
                            sb.append(year);
                        }else {
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                            String year = sdf.format(new Date());
                            sb.append(year);
                        }
                    }else if("[P]".equals(specification)||"[L]".equals(specification)||"[C]".equals(specification)){
                        if(map!=null && !map.isEmpty()){
                            switch (specification){
                                case "[P]":
                                    code = map.get("[P]").toString();
                                    break;
                                case "[L]":
                                    code = map.get("[L]").toString();
                                    break;
                                case "[C]":
                                    code = map.get("[C]").toString();
                                    break;
                            }
                        }
                        //产品料号的长度
                        int length = code.length();
                        //长度不足需要补位
                        if(barcodeLength>length){
                            if(StringUtils.isNotEmpty(fillOperator)){
                                if(0==fillDirection){
                                    for (int i=0;i<barcodeLength-length;i++){
                                        sb.append(fillOperator);
                                    }
                                    sb.append(code);
                                }else {
                                    sb.append(code);
                                    for (int i=0;i<barcodeLength-length;i++){
                                        sb.append(fillOperator);
                                    }
                                }
                            }else {
                                throw new BizErrorException("产品料号/生产线别/客户料号的长度不够，不能没有补位符");
                            }
                            //长度超过需要截取
                        }else if(barcodeLength<length){
                            //截取位置从1开始
                            if(StringUtils.isNotEmpty(interceptPosition)){
                                if(0==interceptDirection){
                                    if(interceptPosition>=barcodeLength){
                                        code.substring(interceptPosition-barcodeLength,interceptPosition);
                                    }else {
                                        throw new BizErrorException("产品料号/生产线别/客户料号从该截取位置截取长度不够");
                                    }
                                }else {
                                    if(interceptDirection+barcodeLength-1<=length){
                                        code.substring(interceptPosition-1,interceptDirection+barcodeLength-1);
                                    }else {
                                        throw new BizErrorException("产品料号/生产线别/客户料号从该截取位置截取长度不够");
                                    }
                                }
                            }else {
                                throw new BizErrorException("产品料号/生产线别/客户料号需要截取是必须有截取位置");
                            }
                        }else {
                            sb.append(code);
                        }
                    }else if("[S]".equals(specification)){
                        String customizeCode="0123456789";
                        maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeCode, String.valueOf(step));
                    }else if("[F]".equals(specification)){
                        String customizeCode="0123456789ABCDEF";
                        maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeCode, getStep(step, customizeCode));
                    }else if("[b]".equals(specification)){
                        customizeValue="0123456789ABCDEFGHJKLMNPQRSTVWXYZ";
                        maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeValue, getStep(step, customizeValue));
                    }else if("[c]".equals(specification)) {
                        customizeValue = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                        maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeValue, getStep(step, customizeValue));
                    }else if("[f]".equals(specification)){
                        //执行函数获取解析码
                        String param = functionResultMap.get(functionName);
                        log.info("====== functionName:" + functionName + "====== param:" + param);
                        if(StringUtils.isEmpty(param)){
                            throw new BizErrorException("条码规则执行函数失败，请检查执行函数");
                        }
                        Map<String,Object> paramMap = JSON.parseObject(param,Map.class);
                        if(Integer.parseInt(paramMap.get("mesCode").toString())!=200){
                            throw new BizErrorException(paramMap.get("message").toString());
                        }else {
                            param = paramMap.get("param").toString();
                        }


                        if(param.length()<barcodeLength){
                            while (param.length()<barcodeLength){
                                StringBuffer s = new StringBuffer();
                                s.append(param).append("0");
                                param = s.toString();
                            }
                        }
                        if(param.length()>barcodeLength){
                            throw new BizErrorException("执行函数返回值超出设定长度");
                        }
                        sb.append(param);
                    }else {  //月、周、日、周的日、年的日、自定义年、月、日、周
                        //String typeCode = CodeUtils.getTypeCode(specification,customizeValue);
                        String typeCode = CodeUtils.getTypeCode(specification,customizeValue,planYear,planMonth,planDay);
                        sb.append(typeCode);
                    }

                    log.info("最大流水号2:"+maxCode+"============");
                }
            }
            // 更新redis最新条码
            boolean b = barcodeRuleUtils.redisUtil.set(key, sb.toString());
            log.info("key:"+key+"============value:"+sb + "================redis 返回值:" + b);
            barcodeList.add(sb.toString());
        }
        log.info("for执行循环 执行总时长 : {}毫秒)",(System.currentTimeMillis() - forqtyTime));

        return barcodeList;
    }

    public static List<String> batchAnalysisCodeByWanbao(List<BaseBarcodeRuleSpec> list, String code,String params,Map<String,Object> map, Integer qty, String key,String planYear,String planMonth,String planDay) {
        long starttime = System.currentTimeMillis();

        String lastBarCode = null;
        boolean hasKey = barcodeRuleUtils.redisUtil.hasKey(key);
        if(hasKey){
            Object redisRuleData = barcodeRuleUtils.redisUtil.get(key);
            lastBarCode = String.valueOf(redisRuleData);
        }else {
            List<String> barcodes = batchAnalysisCode(list, code, params, map, 1, key, planYear, planMonth, planDay);
            lastBarCode = barcodes.get(0);
        }
        log.info("key:"+key+"============ lastBarCode:"+ lastBarCode + "========= hasKey: " + hasKey);
        //获取最大流水号
        String maxCode = getMaxSerialNumber(list, lastBarCode, planYear, planMonth, planDay);
        // 批量生成条码
        Integer start = (StringUtils.isEmpty(map) || Integer.valueOf(maxCode) == 1) ? 0 : Integer.valueOf(maxCode);
        String fixedValue = lastBarCode.substring(0, lastBarCode.length() - maxCode.length());
        log.info("======最大流水号:" + maxCode + "============ 固定值:" + fixedValue + "====== qty: " + qty);
        String returnBarcode = null;
        List<String> barcodeStrList = new ArrayList<>();
        for (int i = 1; i <= qty; i++) {
            String str = String.valueOf((start + i));
            while (true) {
                if (maxCode.length() == str.length()) {
                    break;
                }
                str = "0" + str;
            }
            log.info("======= barcode : " + fixedValue + str);
            barcodeStrList.add(fixedValue + str);
            if (i == qty){
                returnBarcode = fixedValue + str;
            }
        }
        log.info("======== barcodeStrList: " + JSON.toJSON(barcodeStrList));
        // 更新redis最新条码
        boolean b = barcodeRuleUtils.redisUtil.set(key, returnBarcode);
        log.info("============== 生成条码耗时：" + (System.currentTimeMillis() - starttime));
        return barcodeStrList;
    }

    private static synchronized String generateStreamCode(String maxCode, StringBuilder sb, Integer barcodeLength, Integer initialValue, String customizeCode, String step) {
        if (StringUtils.isEmpty(maxCode)) {
            maxCode = changeCode(barcodeLength, initialValue);
            sb.append(maxCode);
        } else {
            String streamCode = CodeUtils.generateSerialNumber(maxCode, step, customizeCode);
            if (streamCode.length() <= barcodeLength) {
                //sb.delete(0,sb.length());
                sb.append(streamCode);
            } else {
                throw new BizErrorException("流水号已经超出定义的范围");
            }
        }
        return maxCode;
    }

    /**
     * 通过最大编号截取最大流水号
     * @param list    条码规则配置
     * @param maxCode 最大编号
     * @return
     */
    public static String getMaxSerialNumber(List<BaseBarcodeRuleSpec> list, String maxCode){
        int sum=0;
        String maxSerialNumber=null;
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            String specification = baseBarcodeRuleSpec.getSpecification();
            String customizeValue = baseBarcodeRuleSpec.getCustomizeValue();
            int length =0;
            if("[G]".equals(specification)){
                length=customizeValue.length();
            }else {
                length = baseBarcodeRuleSpec.getBarcodeLength();
                if(StringUtils.isNotEmpty(maxCode)){
                    Boolean bool = getNewDate(specification,customizeValue,sum,sum+length,maxCode);
                    if(!bool){
                        return null;
                    }
                }
            }
            if("[S]".equals(specification)||"[F]".equals(specification)||"[b]".equals(specification)||"[c]".equals(specification)){
                if(StringUtils.isNotEmpty(maxCode)){
                    if(sum+length>maxCode.length() || sum>maxCode.length()){
                        //maxSerialNumber = maxCode+"1";
                        return null;
                    }else{
                        maxSerialNumber = maxCode.substring(sum, sum + length);
                    }

                }
            }
            sum+=length;
        }
        return maxSerialNumber;
    }

    public static String getMaxSerialNumber(List<BaseBarcodeRuleSpec> list, String maxCode,String planYear,String planMonth,String planDay){
        int sum=0;
        String maxSerialNumber=null;
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            String specification = baseBarcodeRuleSpec.getSpecification();
            String customizeValue = baseBarcodeRuleSpec.getCustomizeValue();
            int length =0;
            if("[G]".equals(specification)){
                length=customizeValue.length();
            }else {
                length = baseBarcodeRuleSpec.getBarcodeLength();
                if(StringUtils.isNotEmpty(maxCode)){
                    Boolean bool = getNewDate(specification,customizeValue,sum,sum+length,maxCode, planYear, planMonth, planDay);
                    if(!bool){
                        return null;
                    }
                }
            }
            if("[S]".equals(specification)||"[F]".equals(specification)||"[b]".equals(specification)||"[c]".equals(specification)){
                if(StringUtils.isNotEmpty(maxCode)){
                    if(sum+length>maxCode.length() || sum>maxCode.length()){
                        //maxSerialNumber = maxCode+"1";
                        return null;
                    }else{
                        maxSerialNumber = maxCode.substring(sum, sum + length);
                    }

                }
            }
            sum+=length;
        }
        return maxSerialNumber;
    }

    private static boolean getNewDate(String specification,String customizeValue,int beginIndex,int endIndex,String maxCode){
        if("[M]".equals(specification)||"[W]".equals(specification)||"[D]".equals(specification)||"[K]".equals(specification)
                ||"[A]".equals(specification)||"[y]".equals(specification)||"[m]".equals(specification)||"[d]".equals(specification)||"[w]".equals(specification)){
            String data = maxCode.substring(beginIndex,endIndex);
            String newData = CodeUtils.getTypeCode(specification,customizeValue);
            if(!data.equals(newData)){
                return false;
            }
        }
        return true;
    }

    private static boolean getNewDate(String specification,String customizeValue,int beginIndex,int endIndex,String maxCode,String planYear,String planMonth,String planDay){
        if("[M]".equals(specification)||"[W]".equals(specification)||"[D]".equals(specification)||"[K]".equals(specification)
                ||"[A]".equals(specification)||"[y]".equals(specification)||"[m]".equals(specification)||"[d]".equals(specification)||"[w]".equals(specification)){
            String data = maxCode.substring(beginIndex,endIndex);
            String newData = CodeUtils.getTypeCode(specification,customizeValue, planYear, planMonth, planDay);
            if(!data.equals(newData)){
                return false;
            }
        }
        return true;
    }

    /**
     * 将步长转成对应进制流水号的字符,例如：36进制的10转成A
     * @param step             步长(步长不大于进制数长度)
     * @param customizeValue   进制数 例如：36进制 customizeValue="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
     * @return
     */
    public static String getStep(Integer step, String customizeValue) {
        Character[] nums = ArrayUtils.toObject(customizeValue.toCharArray());
        List<Character> numbers = Arrays.asList(nums);
        return numbers.get(step).toString();
    }

    /**
     * 初始的流水号
     * @param barcodeLength   长度
     * @param initialValue   初始值
     * @return
     */
    private static String changeCode(Integer barcodeLength, Integer initialValue) {
        int initialLength=0;
        StringBuilder sb=new StringBuilder();
        if(StringUtils.isNotEmpty(initialValue)){
            initialLength = String.valueOf(initialValue).length();
            for (int i=0;i<barcodeLength-initialLength;i++){
                sb.append("0");
            }
            sb.append(initialValue);
        }else {
            for (int i=0;i<barcodeLength-1;i++){
                sb.append("0");
            }
            sb.append("1");
        }

        return sb.toString();
    }
   }


