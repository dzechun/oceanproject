package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
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
            smtBarcodeRuleMapper.insertUseGeneratedKeys(smtBarcodeRule);

            //新增条码规则历史信息
            SmtHtBarcodeRule smtHtBarcodeRule=new SmtHtBarcodeRule();
            BeanUtils.copyProperties(smtBarcodeRule,smtHtBarcodeRule);
            smtHtBarcodeRule.setModifiedUserId(currentUser.getUserId());
            smtHtBarcodeRule.setModifiedTime(new Date());
            int i = smtHtBarcodeRuleMapper.insertSelective(smtHtBarcodeRule);

            return i;
        }

        @Transactional(rollbackFor = Exception.class)
        public void judgeBarcodeRule(SmtBarcodeRule smtBarcodeRule) {
            String barcodeRule = smtBarcodeRule.getBarcodeRule();
            if(StringUtils.isNotEmpty(barcodeRule)){
                //判断Y和y、M和m、D和d、W和w不能同时使用
                boolean yFlag = barcodeRule.contains("Y") && barcodeRule.contains("y");
                boolean mFlag = barcodeRule.contains("M") && barcodeRule.contains("m");
                boolean dFlag = barcodeRule.contains("D") && barcodeRule.contains("d");
                boolean wFlag = barcodeRule.contains("W") && barcodeRule.contains("w");
                if(yFlag||mFlag||dFlag||wFlag){
                    throw new BizErrorException("条码规则配置错误");
                }

                //判断S、F、b、c只能使用一个
                boolean baseCode1 = barcodeRule.contains("S") && !barcodeRule.contains("F") && !barcodeRule.contains("b") && !barcodeRule.contains("c");
                boolean baseCode2 = !barcodeRule.contains("S") && barcodeRule.contains("F") && !barcodeRule.contains("b") && !barcodeRule.contains("c");
                boolean baseCode3 = !barcodeRule.contains("S") && !barcodeRule.contains("F") && barcodeRule.contains("b") && !barcodeRule.contains("c");
                boolean baseCode4 = !barcodeRule.contains("S") && !barcodeRule.contains("F") && !barcodeRule.contains("b") && barcodeRule.contains("c");
                boolean baseCode5 = !barcodeRule.contains("S") && !barcodeRule.contains("F") && !barcodeRule.contains("b") && !barcodeRule.contains("c");
                if(!baseCode1&&!baseCode2&&!baseCode3&&!baseCode4&&!baseCode5){
                    throw new BizErrorException("条码规则配置错误");
                }

                if(barcodeRule.contains("D")||barcodeRule.contains("d")){
                    if(barcodeRule.contains("W")||barcodeRule.contains("w")||barcodeRule.contains("K")||barcodeRule.contains("A")){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }


                //自定义的年月日周的位数是否与条码规则中y、m、d、w的个数相等
                String customizeYear = smtBarcodeRule.getCustomizeYear();
                if(StringUtils.isNotEmpty(customizeYear)){
                    int yearLength = customizeLength(customizeYear);
                    int yCount = countStr(barcodeRule, 'y');
                    if(yearLength!=yCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
                String customizeMonth = smtBarcodeRule.getCustomizeMonth();
                if(StringUtils.isNotEmpty(customizeMonth)){
                    int mouthLength = customizeLength(customizeMonth);
                    int mCount = countStr(barcodeRule, 'm');
                    if(mouthLength!=mCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
                String customizeDay = smtBarcodeRule.getCustomizeDay();
                if(StringUtils.isNotEmpty(customizeDay)){
                    int dayLength = customizeLength(customizeDay);
                    int dCount = countStr(barcodeRule, 'd');
                    if(dayLength!=dCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }
                String customizeWeek = smtBarcodeRule.getCustomizeWeek();
                if(StringUtils.isNotEmpty(customizeWeek)){
                    int weekLength = customizeLength(customizeWeek);
                    int wCount = countStr(barcodeRule, 'w');
                    if(weekLength!=wCount){
                        throw new BizErrorException("条码规则配置错误");
                    }
                }

            }
        }

        @Transactional(rollbackFor = Exception.class)
        public int countStr(String barcodeRule, char str) {
            int count=0;
            char[] charArray = barcodeRule.toCharArray();
            for (int i=0;i<charArray.length;i++){
                if(charArray[i]==str){
                   count++;
                }
            }
            return count;
        }

        @Transactional(rollbackFor = Exception.class)
        public int customizeLength(String field) {
            int length = 0;
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
            smtHtBarcodeRule.setModifiedUserId(currentUser.getUserId());
            smtHtBarcodeRule.setModifiedTime(new Date());
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
        public List<SmtBarcodeRule> findList(SearchSmtBarcodeRule searchSmtBarcodeRule) {
            return smtBarcodeRuleMapper.findList(searchSmtBarcodeRule);
        }

}
