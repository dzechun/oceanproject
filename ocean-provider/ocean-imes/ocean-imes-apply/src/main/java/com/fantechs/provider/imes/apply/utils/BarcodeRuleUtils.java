package com.fantechs.provider.imes.apply.utils;


import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleSpecMapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class BarcodeRuleUtils {
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;

    private static BarcodeRuleUtils barcodeRuleUtils = null;

    private BarcodeRuleUtils(){

    }

    /**
     * 取得BarcodeRuleUtils的单例实现
     *
     * @return
     */
    public static BarcodeRuleUtils getInstance() {
        if (barcodeRuleUtils == null) {
            synchronized (BarcodeRuleUtils.class) {
                if (barcodeRuleUtils == null) {
                    barcodeRuleUtils = new BarcodeRuleUtils();
                }
            }
        }
        return barcodeRuleUtils;
    }

    /**
     *
     * @param list 条码规则配置
     * @param maxCode  已生成的最大流水号
     * @param code 产品料号、生产线别、客户料号
     * @return
     */
    public String analysisCode(List<SmtBarcodeRuleSpec> list,String maxCode,String code) throws IOException {
        StringBuilder sb=new StringBuilder();
        Calendar cal= Calendar.getInstance();
        if(StringUtils.isNotEmpty(list)){
            for (SmtBarcodeRuleSpec smtBarcodeRuleSpec : list) {
                //格式
                String specification = smtBarcodeRuleSpec.getSpecification();
                //长度
                Integer barcodeLength = smtBarcodeRuleSpec.getBarcodeLength();
                //步长
                Integer step = smtBarcodeRuleSpec.getStep();
                //自定义参数值
                String customizeValue = smtBarcodeRuleSpec.getCustomizeValue();
                //补位方向(0.前  1.后)
                Byte fillDirection = smtBarcodeRuleSpec.getFillDirection();
                //补位符
                String fillOperator = smtBarcodeRuleSpec.getFillOperator();
                //截取方向(0.前  1.后)
                Byte interceptDirection = smtBarcodeRuleSpec.getInterceptDirection();
                //截取位置
                Integer interceptPosition = smtBarcodeRuleSpec.getInterceptPosition();
                //初始值
                Integer initialValue = smtBarcodeRuleSpec.getInitialValue();

                if(StringUtils.isEmpty(maxCode)){
                    maxCode=changeCode(barcodeLength,initialValue);
                }

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
                }else if("[M]".equals(specification)){
                    SimpleDateFormat sdf=new SimpleDateFormat("MM");
                    String month = sdf.format(new Date());
                    sb.append(month);
                }else if("[D]".equals(specification)){
                    SimpleDateFormat sdf=new SimpleDateFormat("dd");
                    String day = sdf.format(new Date());
                    sb.append(day);
                }else if("[W]".equals(specification)){
                    int value = cal.get(Calendar.WEEK_OF_YEAR);
                    Format format=new DecimalFormat("00");
                    String week =format.format(value);
                    sb.append(week);
                }else if("[K]".equals(specification)){
                    int value = cal.get(Calendar.DAY_OF_WEEK);
                    sb.append(value);
                }else if("[A]".equals(specification)){
                    int value = cal.get(Calendar.DAY_OF_YEAR);
                    Format format=new DecimalFormat("000");
                    String dayOfYear =format.format(value);
                    sb.append(dayOfYear);
                }else if("[P]".equals(specification)||"[L]".equals(specification)||"[C]".equals(specification)){
                    //产品料号的长度
                    int length = code.length();
                    //长度不足需要补位
                    if(barcodeLength>length){
                        if(StringUtils.isNotEmpty(fillOperator)){
                             if("0".equals(fillDirection)){
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
                        //需要截取
                    }else if(barcodeLength<length){
                         //截取位置从0开始
                         if(StringUtils.isNotEmpty(interceptPosition)){
                             if("0".equals(interceptDirection)){
                                 if(interceptPosition+1>=barcodeLength){
                                     code.substring(interceptPosition+1-barcodeLength,interceptPosition);
                                 }else {
                                     throw new BizErrorException("产品料号/生产线别/客户料号从该截取位置截取长度不够");
                                 }
                             }else {
                                 if(interceptDirection+barcodeLength<=length){
                                      code.substring(interceptPosition,interceptDirection+barcodeLength-1);
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
                    String stepLength = String.valueOf(step);
                    String streamCode= CodeUtils.generateSerialNumber(maxCode,stepLength,customizeCode);
                    if(streamCode.length()<=barcodeLength){
                        sb.append(streamCode);
                    }else {
                        throw new BizErrorException("流水号已经超出定义的范围");
                    }
                }else if("[F]".equals(specification)){
                    String customizeCode="0123456789ABCDEF";
                    String stepLength = String.valueOf(step);
                    String streamCode= CodeUtils.generateSerialNumber(maxCode,stepLength,customizeCode);
                    if(streamCode.length()<=barcodeLength){
                        sb.append(streamCode);
                    }else {
                        throw new BizErrorException("流水号已经超出定义的范围");
                    }
                }else if("[b]".equals(specification)||"[c]".equals(specification)){
                   // String customizeValue="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    //step小于10
                    String stepLength = String.valueOf(step);
                    String streamCode= CodeUtils.generateSerialNumber(maxCode,stepLength,customizeValue);
                    if(streamCode.length()<=barcodeLength){
                        sb.append(streamCode);
                    }else {
                        throw new BizErrorException("流水号已经超出定义的范围");
                    }
                }else if("[y]".equals(specification)){
                    String value=null;
                    Map<String, Object> map = JsonUtils.jsonToMap(customizeValue);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                    String year = sdf.format(new Date());
                    for(String key : map.keySet()){
                        if(key.equals(year)){
                            value = (String) map.get(key);
                        }
                    }
                    sb.append(value);
                }else if("[m]".equals(specification)){
                    String value=null;
                    Map<String, Object> map = JsonUtils.jsonToMap(customizeValue);
                    int m = cal.get(Calendar.MONTH) + 1;
                    String month = String.valueOf(m);
                    for(String key : map.keySet()){
                        if(key.equals(month)){
                            value = (String) map.get(key);
                        }
                    }
                    sb.append(value);
                }else if("[d]".equals(specification)){
                    String value=null;
                    Map<String, Object> map = JsonUtils.jsonToMap(customizeValue);
                    int d = cal.get(Calendar.DAY_OF_MONTH) + 1;
                    String day = String.valueOf(d);
                    for(String key : map.keySet()){
                        if(key.equals(day)){
                            value = (String) map.get(key);
                        }
                    }
                    sb.append(value);
                }else if("[w]".equals(specification)){
                    String value=null;
                    Map<String, Object> map = JsonUtils.jsonToMap(customizeValue);
                    int w = cal.get(Calendar.WEEK_OF_YEAR) + 1;
                    String day = String.valueOf(w);
                    for(String key : map.keySet()){
                        if(key.equals(day)){
                            value = (String) map.get(key);
                        }
                    }
                    sb.append(value);
                }
            }
        }

        return sb.toString();
    }

    /**
     * 初始的流水号
     * @param barcodeLength
     * @param initialValue
     * @return
     */
    private String changeCode(Integer barcodeLength, Integer initialValue) {
        StringBuilder sb=new StringBuilder();
        int initialLength = String.valueOf(initialValue).length();
        for (int i=0;i<barcodeLength-initialLength;i++){
            sb.append("0");
        }
        sb.append(initialValue);
        return sb.toString();
    }

    public static void main(String[] args) throws ParseException, IOException {
       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = "2020-01-03";
        Date date = sdf.parse(str);
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);

        int Y = cal.get(Calendar.YEAR);
        int M = cal.get(Calendar.MONTH)+1;
        int D = cal.get(Calendar.DAY_OF_MONTH);
        int W = cal.get(Calendar.WEEK_OF_YEAR);
        int K = cal.get(Calendar.DAY_OF_WEEK);
        int A = cal.get(Calendar.DAY_OF_YEAR);

        Format f1=new DecimalFormat("000");
        String  count =f1.format(A);
        System.out.println("Y="+Y);
        System.out.println("M="+M);
        System.out.println("D="+D);
        System.out.println("W="+W);
        System.out.println("K="+K);
        System.out.println("A="+A);
        System.out.println("count="+count);*/


        String str="{\"2020\": \"H\",\"2021\": \"I\",\"2022\": \"J\",\"2023\": \"K\",\"2024\": \"L\"}";
        String value=null;
        Map<String, Object> map = JsonUtils.jsonToMap(str);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());
        for(String key : map.keySet()){
            if(key.equals(year)){
                value = (String) map.get(key);
            }
        }
        System.out.println(value);

    }
}


