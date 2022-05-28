package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRule;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleMapper;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleSpecMapper;
import com.fantechs.provider.base.mapper.BaseHtBarcodeRuleMapper;
import com.fantechs.provider.base.mapper.BaseLabelCategoryMapper;
import com.fantechs.provider.base.service.BaseBarcodeRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by wcz on 2020/10/26.
 */
@Service
public class BaseBarcodeRuleServiceImpl extends BaseService<BaseBarcodeRule> implements BaseBarcodeRuleService {

    @Resource
    private BaseBarcodeRuleMapper baseBarcodeRuleMapper;
    @Resource
    private BaseHtBarcodeRuleMapper baseHtBarcodeRuleMapper;
    @Resource
    private BaseBarcodeRuleSpecMapper baseBarcodeRuleSpecMapper;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private BaseLabelCategoryMapper baseLabelCategoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseBarcodeRule baseBarcodeRule) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseBarcodeRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("barcodeRuleCode", baseBarcodeRule.getBarcodeRuleCode());

        List<BaseBarcodeRule> baseBarcodeRules = baseBarcodeRuleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseBarcodeRules)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();
        criteria.andEqualTo("labelCategoryId",baseBarcodeRule.getLabelCategoryId()).andEqualTo("barcodeRule",baseBarcodeRule.getBarcodeRule());
        baseBarcodeRules = baseBarcodeRuleMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseBarcodeRules)){
            throw new BizErrorException("同一类别无法设置同规则");
        }

        baseBarcodeRule.setCreateUserId(currentUser.getUserId());
        baseBarcodeRule.setCreateTime(new Date());
        baseBarcodeRule.setModifiedUserId(currentUser.getUserId());
        baseBarcodeRule.setModifiedTime(new Date());
        baseBarcodeRule.setOrganizationId(currentUser.getOrganizationId());
        int i = baseBarcodeRuleMapper.insertUseGeneratedKeys(baseBarcodeRule);

        //新增条码规则历史信息
        BaseHtBarcodeRule baseHtBarcodeRule =new BaseHtBarcodeRule();
        BeanUtils.copyProperties(baseBarcodeRule, baseHtBarcodeRule);
        baseHtBarcodeRuleMapper.insertSelective(baseHtBarcodeRule);

        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseBarcodeRule baseBarcodeRule) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseBarcodeRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("barcodeRuleCode", baseBarcodeRule.getBarcodeRuleCode());

        BaseBarcodeRule rule = baseBarcodeRuleMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(rule)&&!rule.getBarcodeRuleId().equals(baseBarcodeRule.getBarcodeRuleId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseBarcodeRule.setModifiedUserId(currentUser.getUserId());
        baseBarcodeRule.setModifiedTime(new Date());
        baseBarcodeRule.setOrganizationId(currentUser.getOrganizationId());
        int i = baseBarcodeRuleMapper.updateByPrimaryKeySelective(baseBarcodeRule);


        //新增条码规则历史信息
        BaseHtBarcodeRule baseHtBarcodeRule =new BaseHtBarcodeRule();
        BeanUtils.copyProperties(baseBarcodeRule, baseHtBarcodeRule);
        baseHtBarcodeRuleMapper.insertSelective(baseHtBarcodeRule);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<BaseHtBarcodeRule> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] barcodeRuleIds = ids.split(",");
        for (String barcodeRuleId : barcodeRuleIds) {
            BaseBarcodeRule baseBarcodeRule = baseBarcodeRuleMapper.selectByPrimaryKey(barcodeRuleId);
            if(StringUtils.isEmpty(baseBarcodeRule)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增条码规则历史信息
            BaseHtBarcodeRule baseHtBarcodeRule =new BaseHtBarcodeRule();
            BeanUtils.copyProperties(baseBarcodeRule, baseHtBarcodeRule);
            baseHtBarcodeRule.setModifiedUserId(currentUser.getUserId());
            baseHtBarcodeRule.setModifiedTime(new Date());
            list.add(baseHtBarcodeRule);

            /**
             * 同时删除该条码规则的配置
             */
            Example example = new Example(BaseBarcodeRuleSpec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleId", baseBarcodeRule.getBarcodeRuleId());
            baseBarcodeRuleSpecMapper.deleteByExample(example);
        }
        baseHtBarcodeRuleMapper.insertList(list);


        return baseBarcodeRuleMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseBarcodeRuleDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseBarcodeRuleMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int preserve(BaseBarcodeRule baseBarcodeRule) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int i=0;
        Long barcodeRuleId = baseBarcodeRule.getBarcodeRuleId();
        if(StringUtils.isEmpty(barcodeRuleId)){
            //新增条码规则
            //判断条码规则编码是否重复
            Example example = new Example(BaseBarcodeRule.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("barcodeRuleCode", baseBarcodeRule.getBarcodeRuleCode());
            BaseBarcodeRule baseBarcodeRule1 = baseBarcodeRuleMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseBarcodeRule1)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            example.clear();

            //每个组织只能有一个默认规则
            SearchSysSpecItem lableItem = new SearchSysSpecItem();
            lableItem.setSpecCode("BaseLabel");
            ResponseEntity<List<SysSpecItem>> specItemList = securityFeignApi.findSpecItemList(lableItem);
            Map<String,Object> map = new HashMap();
            map.put("labelCategoryCode",specItemList.getData().get(0).getParaValue());
            map.put("codeQueryMark",(byte)1);
            map.put("orgId",currentUser.getOrganizationId());
            List<BaseLabelCategoryDto> baseLabelCategoryDtos = baseLabelCategoryMapper.findList(map);
            if(StringUtils.isNotEmpty(baseLabelCategoryDtos)) {
                Map<String,Object> map1 = new HashMap();
                map1.put("labelCategoryId",baseLabelCategoryDtos.get(0).getLabelCategoryId());
                List<BaseBarcodeRuleDto> barcodeRulList = baseBarcodeRuleMapper.findList(map1);
                //  baseLabelCategoryDtos.get(0).getLabelCategoryId()
                if (StringUtils.isNotEmpty(barcodeRulList) && baseBarcodeRule.getLabelCategoryId().equals(barcodeRulList.get(0).getLabelCategoryId())) {
                    throw new BizErrorException("每个组织只能有一个默认规则");
                }
            }

            baseBarcodeRule.setCreateTime(new Date());
            baseBarcodeRule.setCreateUserId(currentUser.getUserId());
            baseBarcodeRule.setOrganizationId(currentUser.getOrganizationId());
            i = baseBarcodeRuleMapper.insertUseGeneratedKeys(baseBarcodeRule);

            /**
             * 保存条码规则配置
             */
            List<BaseBarcodeRuleSpec> list = baseBarcodeRule.getBarcodeRuleSpecs();
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("条码规则没有配置");
            }

            //校验设置的条码规则是否符合
            String barcodeRule = checkBarcodeRule(list, baseBarcodeRule);

            //配置好条码规则后，设置进条码规则中
            baseBarcodeRule.setBarcodeRule(barcodeRule);
            this.update(baseBarcodeRule);

            //同一条码规则类别，条码规则不能重复
            this.ifBarcodeRuleRepeat(baseBarcodeRule);

            for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
                baseBarcodeRuleSpec.setFillDirection(StringUtils.isEmpty(baseBarcodeRuleSpec.getFillDirection()) ? (byte)0 : baseBarcodeRuleSpec.getFillDirection());
                baseBarcodeRuleSpec.setInterceptDirection(StringUtils.isEmpty(baseBarcodeRuleSpec.getInterceptDirection()) ? 0 : baseBarcodeRuleSpec.getFillDirection());
            }
            baseBarcodeRuleSpecMapper.insertList(list);
        }else {

            /**
             * 删除原有配置，保存现在的条码规则配置
             */
            Example example = new Example(BaseBarcodeRuleSpec.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleId", baseBarcodeRule.getBarcodeRuleId());
            baseBarcodeRuleSpecMapper.deleteByExample(example);

            List<BaseBarcodeRuleSpec> list = baseBarcodeRule.getBarcodeRuleSpecs();
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("条码规则没有配置");
            }

            //校验设置的条码规则是否符合
            String barcodeRule = checkBarcodeRule(list, baseBarcodeRule);

            //配置好条码规则后，设置进条码规则中
            baseBarcodeRule.setBarcodeRule(barcodeRule);
            i = this.update(baseBarcodeRule);

            //同一条码规则类别，条码规则不能重复
           this.ifBarcodeRuleRepeat(baseBarcodeRule);

            baseBarcodeRuleSpecMapper.insertList(list);
        }
        return i;
    }

    public void ifBarcodeRuleRepeat(BaseBarcodeRule baseBarcodeRule){
        Example example1 = new Example(BaseBarcodeRule.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("labelCategoryId",baseBarcodeRule.getLabelCategoryId())
                .andEqualTo("barcodeRule",baseBarcodeRule.getBarcodeRule())
                .andEqualTo("organizationId",baseBarcodeRule.getOrganizationId())
                .andNotEqualTo("barcodeRuleId",baseBarcodeRule.getBarcodeRuleId());
        BaseBarcodeRule baseBarcodeRule2 = baseBarcodeRuleMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseBarcodeRule2)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"同一条码规则类别，条码规则不能重复");
        }
    }

    @Override
    public List<BaseBarcodeRule> findListByBarcodeRuleCategoryIds(List<Long> ids) {
        Example example = new Example(BaseBarcodeRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("barcodeRuleCategoryId", ids);
        return baseBarcodeRuleMapper.selectByExample(example);
    }


    @Transactional(rollbackFor = Exception.class)
    public String checkBarcodeRule(List<BaseBarcodeRuleSpec> list, BaseBarcodeRule baseBarcodeRule) {
        List<String> specs=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<list.size();i++){
            BaseBarcodeRuleSpec baseBarcodeRuleSpec = list.get(i);

            String specification = baseBarcodeRuleSpec.getSpecification();
            Integer barcodeLength = baseBarcodeRuleSpec.getBarcodeLength();
            String customizeValue = baseBarcodeRuleSpec.getCustomizeValue();
            Integer initialValue = baseBarcodeRuleSpec.getInitialValue();
            Integer step = baseBarcodeRuleSpec.getStep();
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
            if(specification.contains("S")||specification.contains("F")){
                if(StringUtils.isEmpty(initialValue)||initialValue<1){
                    throw new BizErrorException("初始值不能为空必须大于0");
                }
                if(StringUtils.isEmpty(step)||step<1){
                    throw new BizErrorException("步长不能为空必须大于0");
                }
            }
            baseBarcodeRuleSpec.setBarcodeRuleId(baseBarcodeRule.getBarcodeRuleId());
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
