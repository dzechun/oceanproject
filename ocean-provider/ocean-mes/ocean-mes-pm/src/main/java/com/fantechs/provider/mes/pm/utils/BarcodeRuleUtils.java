package com.fantechs.provider.mes.pm.utils;


import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wcz
 */
@Component
public class BarcodeRuleUtils {

    /**
     *
     * @param list 条码规则配置
     * @param maxCode  已生成的最大流水号
     * @param code 产品料号、生产线别、客户料号
     * @return
     */
    public static String analysisCode(List<SmtBarcodeRuleSpec> list,String maxCode,String code){
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
                }else if("[c]".equals(specification)){
                    customizeValue="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    maxCode = generateStreamCode(maxCode, sb, barcodeLength, initialValue, customizeValue, getStep(step, customizeValue));
                }else {  //月、周、日、周的日、年的日、自定义年、月、日、周
                    String typeCode = CodeUtils.getTypeCode(specification,customizeValue);
                    sb.append(typeCode);
                }
            }
        }

        return sb.toString();
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
    public static String getMaxSerialNumber(List<SmtBarcodeRuleSpec> list,String maxCode){
        int sum=0;
        String maxSerialNumber=null;
        for (SmtBarcodeRuleSpec smtBarcodeRuleSpec : list) {
            String specification = smtBarcodeRuleSpec.getSpecification();
            String customizeValue = smtBarcodeRuleSpec.getCustomizeValue();
            int length =0;
            if("[G]".equals(specification)){
                length=customizeValue.length();
            }else {
                length = smtBarcodeRuleSpec.getBarcodeLength();
            }
            if("[S]".equals(specification)||"[F]".equals(specification)||"[b]".equals(specification)||"[c]".equals(specification)){
                if(StringUtils.isNotEmpty(maxCode)){
                    if(sum+length>maxCode.length() || sum>maxCode.length()){
                        maxSerialNumber = maxCode+"1";
                    }else{
                        maxSerialNumber = maxCode.substring(sum, sum + length);
                    }

                }
            }
            sum+=length;
        }
        return maxSerialNumber;
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


