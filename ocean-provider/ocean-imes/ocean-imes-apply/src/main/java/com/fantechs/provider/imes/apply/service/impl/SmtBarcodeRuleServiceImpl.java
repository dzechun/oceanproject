package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtBarcodeRuleDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleMapper;
import com.fantechs.provider.imes.apply.mapper.SmtHtBarcodeRuleMapper;
import com.fantechs.provider.imes.apply.service.SmtBarcodeRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by wcz on 2020/10/26.
 */
@Service
public class SmtBarcodeRuleServiceImpl extends BaseService<SmtBarcodeRule> implements SmtBarcodeRuleService {

    @Resource
    private SmtBarcodeRuleMapper smtBarcodeRuleMapper;
    @Resource
    private SmtHtBarcodeRuleMapper smtHtBarcodeRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtBarcodeRule smtBarcodeRule) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtBarcodeRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcodeRuleCode",smtBarcodeRule.getBarcodeRuleCode());

        List<SmtBarcodeRule> smtBarcodeRules = smtBarcodeRuleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtBarcodeRules)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //判断条码规则是否符合规定
        judgeBarcodeRule(smtBarcodeRule);

        smtBarcodeRule.setCreateUserId(currentUser.getUserId());
        smtBarcodeRule.setCreateTime(new Date());
        smtBarcodeRule.setModifiedUserId(currentUser.getUserId());
        smtBarcodeRule.setModifiedTime(new Date());
        smtBarcodeRuleMapper.insertUseGeneratedKeys(smtBarcodeRule);

        //新增条码规则历史信息
        SmtHtBarcodeRule smtHtBarcodeRule=new SmtHtBarcodeRule();
        BeanUtils.copyProperties(smtBarcodeRule,smtHtBarcodeRule);
        int i = smtHtBarcodeRuleMapper.insertSelective(smtHtBarcodeRule);

        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    public void judgeBarcodeRule(SmtBarcodeRule smtBarcodeRule) {
        String barcodeRule = smtBarcodeRule.getBarcodeRule();
        if(StringUtils.isNotEmpty(barcodeRule)){
            List<String> list = getVariable(barcodeRule);
            StringBuilder sb=new StringBuilder();
            for (String variable : list) {
                sb.append(variable);
                //判断Y和y、M和m、D和d、W和w不能同时使用
                boolean yFlag = variable.contains("Y") && variable.contains("y");
                boolean mFlag = variable.contains("M") && variable.contains("m");
                boolean dFlag = variable.contains("D") && variable.contains("d");
                boolean wFlag = variable.contains("W") && variable.contains("w");
                if(yFlag||mFlag||dFlag||wFlag){
                    throw new BizErrorException("条码规则配置错误");
                }


                //自定义的年月日周的位数是否与条码规则中y、m、d、w的个数相等
                String customizeYear = smtBarcodeRule.getCustomizeYear();
                if(StringUtils.isNotEmpty(customizeYear)){
                    int yearLength = customizeLength(customizeYear);
                    int yCount = countStr(variable, 'y');
                    if(yearLength!=yCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
                String customizeMonth = smtBarcodeRule.getCustomizeMonth();
                if(StringUtils.isNotEmpty(customizeMonth)){
                    int mouthLength = customizeLength(customizeMonth);
                    int mCount = countStr(variable, 'm');
                    if(mouthLength!=mCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
                String customizeDay = smtBarcodeRule.getCustomizeDay();
                if(StringUtils.isNotEmpty(customizeDay)){
                    int dayLength = customizeLength(customizeDay);
                    int dCount = countStr(variable, 'd');
                    if(dayLength!=dCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
                String customizeWeek = smtBarcodeRule.getCustomizeWeek();
                if(StringUtils.isNotEmpty(customizeWeek)){
                    int weekLength = customizeLength(customizeWeek);
                    int wCount = countStr(variable, 'w');
                    if(weekLength!=wCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
            }
            //判断S、F、b、c只能使用一个
            boolean sCode = sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            boolean fCode = !sb.toString().contains("S") && sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            boolean bCode = !sb.toString().contains("S") && !sb.toString().contains("F") && sb.toString().contains("b") && !sb.toString().contains("c");
            boolean cCode = !sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && sb.toString().contains("c");
            boolean baseCode = !sb.toString().contains("S") && !sb.toString().contains("F") && !sb.toString().contains("b") && !sb.toString().contains("c");
            if(!sCode&&!fCode&&!bCode&&!cCode&&!baseCode){
                throw new BizErrorException("条码规则配置错误");
            }

        }
    }

    /**
     * 获取条码规则中的变量
     * @param barcodeRule
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> getVariable(String barcodeRule){
        List<String> list=new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(.*?)]");
        Matcher matcher = pattern.matcher(barcodeRule);
        while((matcher.find())){
            list.add(matcher.group(1));
        }
        return list;
    }

    /**
     * 统计某一个字符出现的次数
     * @param variable
     * @param str
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int countStr(String variable, char str) {
        int count=0;
        char[] charArray = variable.toCharArray();
        for (int i=0;i<charArray.length;i++){
            if(charArray[i]==str){
                count++;
            }
        }
        return count;
    }

    /**
     * 统计自定义属性value的长度
     * @param field
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int customizeLength(String field) {
        int length = 0;
        field = field.substring(1, field.length() - 1);
        if(StringUtils.isNotEmpty(field)){
            String[] fieldJson = field.split(",");
            String[] fields = fieldJson[0].split(":");
            length = fields[1].length();
        }
        return length;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtBarcodeRule smtBarcodeRule) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtBarcodeRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcodeRuleCode",smtBarcodeRule.getBarcodeRuleCode());

        SmtBarcodeRule barcodeRule = smtBarcodeRuleMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(barcodeRule)&&!barcodeRule.getBarcodeRuleId().equals(smtBarcodeRule.getBarcodeRuleId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //判断条码规则是否符合规定
        judgeBarcodeRule(smtBarcodeRule);

        smtBarcodeRule.setModifiedUserId(currentUser.getUserId());
        smtBarcodeRule.setModifiedTime(new Date());
        int i = smtBarcodeRuleMapper.updateByPrimaryKeySelective(smtBarcodeRule);


        //新增条码规则历史信息
        SmtHtBarcodeRule smtHtBarcodeRule=new SmtHtBarcodeRule();
        BeanUtils.copyProperties(smtBarcodeRule,smtHtBarcodeRule);
        smtHtBarcodeRuleMapper.insertSelective(smtHtBarcodeRule);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<SmtHtBarcodeRule> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] barcodeRuleIds = ids.split(",");
        for (String barcodeRuleId : barcodeRuleIds) {
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(barcodeRuleId);
            if(StringUtils.isEmpty(smtBarcodeRule)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增条码规则历史信息
            SmtHtBarcodeRule smtHtBarcodeRule=new SmtHtBarcodeRule();
            BeanUtils.copyProperties(smtBarcodeRule,smtHtBarcodeRule);
            smtHtBarcodeRule.setModifiedUserId(currentUser.getUserId());
            smtHtBarcodeRule.setModifiedTime(new Date());
            list.add(smtHtBarcodeRule);

        }
        smtHtBarcodeRuleMapper.insertList(list);

        return smtBarcodeRuleMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtBarcodeRuleDto> findList(SearchSmtBarcodeRule searchSmtBarcodeRule) {
        return smtBarcodeRuleMapper.findList(searchSmtBarcodeRule);
    }

}
