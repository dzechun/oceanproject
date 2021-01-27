package com.fantechs.provider.mes.pm.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRule;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtBarcodeRule;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.SmtBarcodeRuleMapper;
import com.fantechs.provider.mes.pm.mapper.SmtBarcodeRuleSpecMapper;
import com.fantechs.provider.mes.pm.mapper.SmtHtBarcodeRuleMapper;
import com.fantechs.provider.mes.pm.service.SmtBarcodeRuleService;
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
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;

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


        smtBarcodeRule.setCreateUserId(currentUser.getUserId());
        smtBarcodeRule.setCreateTime(new Date());
        smtBarcodeRule.setModifiedUserId(currentUser.getUserId());
        smtBarcodeRule.setModifiedTime(new Date());
        int i = smtBarcodeRuleMapper.insertUseGeneratedKeys(smtBarcodeRule);

        //新增条码规则历史信息
        SmtHtBarcodeRule smtHtBarcodeRule=new SmtHtBarcodeRule();
        BeanUtils.copyProperties(smtBarcodeRule,smtHtBarcodeRule);
        smtHtBarcodeRuleMapper.insertSelective(smtHtBarcodeRule);

        return i;
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

        SmtBarcodeRule rule = smtBarcodeRuleMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(rule)&&!rule.getBarcodeRuleId().equals(smtBarcodeRule.getBarcodeRuleId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

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

            /**
             * 同时删除该条码规则的配置
             */
            Example example = new Example(SmtBarcodeRuleSpec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleId",smtBarcodeRule.getBarcodeRuleId());
            smtBarcodeRuleSpecMapper.deleteByExample(example);
        }
        smtHtBarcodeRuleMapper.insertList(list);


        return smtBarcodeRuleMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtBarcodeRuleDto> findList(SearchSmtBarcodeRule searchSmtBarcodeRule) {
        return smtBarcodeRuleMapper.findList(searchSmtBarcodeRule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int preserve(SmtBarcodeRule smtBarcodeRule) {
        int i=0;
        Long barcodeRuleId = smtBarcodeRule.getBarcodeRuleId();
        if(StringUtils.isEmpty(barcodeRuleId)){
            //新增条码规则
            i = smtBarcodeRuleMapper.insertUseGeneratedKeys(smtBarcodeRule);

            /**
             * 保存条码规则配置
             */
            List<SmtBarcodeRuleSpec> list = smtBarcodeRule.getBarcodeRuleSpecs();
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("条码规则没有配置");
            }

            //校验设置的条码规则是否符合
            String barcodeRule = checkBarcodeRule(list,smtBarcodeRule);

            //配置好条码规则后，设置进条码规则中
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            this.update(smtBarcodeRule);
            smtBarcodeRuleSpecMapper.insertList(list);
        }else {

            /**
             * 删除原有配置，保存现在的条码规则配置
             */
            Example example = new Example(SmtBarcodeRuleSpec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleId",smtBarcodeRule.getBarcodeRuleId());
            smtBarcodeRuleSpecMapper.deleteByExample(example);

            List<SmtBarcodeRuleSpec> list = smtBarcodeRule.getBarcodeRuleSpecs();
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("条码规则没有配置");
            }

            //校验设置的条码规则是否符合
            String barcodeRule = checkBarcodeRule(list,smtBarcodeRule);

            //配置好条码规则后，设置进条码规则中
            smtBarcodeRule.setBarcodeRule(barcodeRule);
            i = this.update(smtBarcodeRule);
            smtBarcodeRuleSpecMapper.insertList(list);
        }
        return i;
    }


    @Transactional(rollbackFor = Exception.class)
    public String checkBarcodeRule(List<SmtBarcodeRuleSpec> list,SmtBarcodeRule smtBarcodeRule) {
        List<String> specs=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<list.size();i++){
            SmtBarcodeRuleSpec smtBarcodeRuleSpec = list.get(i);

            String specification = smtBarcodeRuleSpec.getSpecification();
            Integer barcodeLength = smtBarcodeRuleSpec.getBarcodeLength();
            String customizeValue = smtBarcodeRuleSpec.getCustomizeValue();
            Integer initialValue = smtBarcodeRuleSpec.getInitialValue();
            Integer step = smtBarcodeRuleSpec.getStep();
            if(specification.contains("G")){
                sb.append(customizeValue);
            }else {
                //例如：将[Y][Y][Y][Y]转成[YYYY]
                String spec = getRuleSpec(specification, barcodeLength);
                sb.append(spec);
            }
            if(specification.contains("S")){
                if(barcodeLength>10){
                    throw new BizErrorException("十进制长度不能超过10");
                }
            }else if (specification.contains("F")){
                if (barcodeLength>16){
                    throw new BizErrorException("十六进制长度不能超过16");
                }
            }

            if(StringUtils.isEmpty(initialValue)||initialValue<1){
                throw new BizErrorException("初始值不能为空必须大于0");
            }
            if(StringUtils.isEmpty(step)||step<1){
                throw new BizErrorException("步长不能为空必须大于0");
            }
            smtBarcodeRuleSpec.setBarcodeRuleId(smtBarcodeRule.getBarcodeRuleId());
            specs.add(specification);
        }

        //判断S、F、b、c只能使用一个
        boolean sCode = specs.contains("S") && !specs.contains("F") && !specs.contains("b") && !specs.contains("c");
        boolean fCode = !specs.contains("S") && specs.contains("F") && !specs.contains("b") && !specs.contains("c");
        boolean bCode = !specs.contains("S") && !specs.contains("F") && specs.contains("b") && !specs.contains("c");
        boolean cCode = !specs.contains("S") && !specs.contains("F") && !specs.contains("b") && specs.contains("c");
        boolean baseCode = !specs.contains("S") && !specs.contains("F") && !specs.contains("b") && !specs.contains("c");
        if(!sCode&&!fCode&&!bCode&&!cCode&&!baseCode){
            throw new BizErrorException("条码规则配置错误");
        }

        //specs 包含多少个[P]属性
        long materialNum = specs.stream().filter("[P]"::equals).count();
        //specs 包含多少个[L]属性
        long lineNum = specs.stream().filter("[L]"::equals).count();
        //specs 包含多少个[C]属性
        long customerNum = specs.stream().filter("[C]"::equals).count();

        if(materialNum>1||lineNum>1||customerNum>1){
            throw new BizErrorException("条码规则配置错误");
        }
        return sb.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    public String getRuleSpec(String specification, Integer barcodeLength) {
        StringBuilder sb=new StringBuilder();
        for (int j=0;j<barcodeLength;j++){
            sb.append(specification);
        }

        Pattern pattern = Pattern.compile("](.*?)\\[");
        Matcher matcher = pattern.matcher(sb.toString());
        String spec = matcher.replaceAll("");
        return spec;
    }
}
