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
