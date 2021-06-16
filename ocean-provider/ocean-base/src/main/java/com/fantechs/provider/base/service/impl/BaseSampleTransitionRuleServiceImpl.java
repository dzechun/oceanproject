package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRule;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRuleDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleTransitionRule;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSampleTransitionRuleMapper;
import com.fantechs.provider.base.mapper.BaseSampleTransitionRuleDetMapper;
import com.fantechs.provider.base.mapper.BaseSampleTransitionRuleMapper;
import com.fantechs.provider.base.service.BaseSampleTransitionRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BaseSampleTransitionRuleServiceImpl extends BaseService<BaseSampleTransitionRule> implements BaseSampleTransitionRuleService {

    @Resource
    private BaseSampleTransitionRuleMapper baseSampleTransitionRuleMapper;

    @Resource
    private BaseHtSampleTransitionRuleMapper baseHtSampleTransitionRuleMapper;

    @Resource
    private BaseSampleTransitionRuleDetMapper baseSampleTransitionRuleDetMapper;

    @Override
    public List<BaseSampleTransitionRuleDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseSampleTransitionRuleMapper.findList(map);
    }

    @Override
    public int save(BaseSampleTransitionRule record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());
        record.setOrgId(user.getOrganizationId());

        int i = baseSampleTransitionRuleMapper.insertUseGeneratedKeys(record);
        if (StringUtils.isNotEmpty(record.getList())){
            for (BaseSampleTransitionRuleDet baseSampleTransitionRuleDet : record.getList()) {
                baseSampleTransitionRuleDet.setSampleTransitionRuleId(record.getSampleTransitionRuleId());
            }
            baseSampleTransitionRuleDetMapper.insertList(record.getList());
        }

        BaseHtSampleTransitionRule baseHtSampleTransitionRule = new BaseHtSampleTransitionRule();
        BeanUtils.copyProperties(record,baseHtSampleTransitionRule);
        baseHtSampleTransitionRuleMapper.insert(baseHtSampleTransitionRule);

        return i;
    }

    @Override
    public int update(BaseSampleTransitionRule entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());

        BaseHtSampleTransitionRule baseHtSampleTransitionRule = new BaseHtSampleTransitionRule();
        BeanUtils.copyProperties(entity,baseHtSampleTransitionRule);
        baseHtSampleTransitionRuleMapper.insert(baseHtSampleTransitionRule);

        Example example = new Example(BaseSampleTransitionRuleDet.class);
        example.createCriteria().andEqualTo("sampleTransitionRuleId",entity.getSampleTransitionRuleId());
        baseSampleTransitionRuleDetMapper.deleteByExample(example);

        if (StringUtils.isNotEmpty(entity.getList())){
            baseSampleTransitionRuleDetMapper.insertList(entity.getList());
        }

        return baseSampleTransitionRuleMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtSampleTransitionRule> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");

        Example example = new Example(BaseSampleTransitionRuleDet.class);
        for (String id : idsArr) {
            BaseSampleTransitionRule baseSampleTransitionRule = baseSampleTransitionRuleMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSampleTransitionRule)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            example.createCriteria().andEqualTo("sampleTransitionRuleId",id);
            baseSampleTransitionRuleDetMapper.deleteByExample(example);

            BaseHtSampleTransitionRule baseHtSampleTransitionRule = new BaseHtSampleTransitionRule();
            BeanUtils.copyProperties(baseSampleTransitionRule,baseHtSampleTransitionRule);
            list.add(baseHtSampleTransitionRule);
        }

        baseHtSampleTransitionRuleMapper.insertList(list);
        return baseSampleTransitionRuleMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseSampleTransitionRule entity){
        Example example = new Example(BaseSampleTransitionRule.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("sampleTransitionRuleCode",entity.getSampleTransitionRuleCode());
        if (StringUtils.isNotEmpty(entity.getSampleTransitionRuleId())){
            criteria.andNotEqualTo("sampleTransitionRuleId",entity.getSampleTransitionRuleId());
        }
        BaseSampleTransitionRule baseSampleTransitionRule = baseSampleTransitionRuleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSampleTransitionRule)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }


}
