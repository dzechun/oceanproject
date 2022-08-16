package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRule;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleDet;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRule;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRuleDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInAndOutRuleDetMapper;
import com.fantechs.provider.base.mapper.BaseHtInAndOutRuleMapper;
import com.fantechs.provider.base.mapper.BaseInAndOutRuleDetMapper;
import com.fantechs.provider.base.mapper.BaseInAndOutRuleMapper;
import com.fantechs.provider.base.service.BaseInAndOutRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/14.
 */
@Service
public class BaseInAndOutRuleServiceImpl extends BaseService<BaseInAndOutRule> implements BaseInAndOutRuleService {

    @Resource
    private BaseInAndOutRuleMapper baseInAndOutRuleMapper;
    @Resource
    private BaseInAndOutRuleDetMapper baseInAndOutRuleDetMapper;
    @Resource
    private BaseHtInAndOutRuleMapper baseHtInAndOutRuleMapper;

    @Override
    public List<BaseInAndOutRule> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseInAndOutRule> baseInAndOutRules = baseInAndOutRuleMapper.findList(map);
        SearchBaseInAndOutRuleDet searchBaseInAndOutRuleDet = new SearchBaseInAndOutRuleDet();

        for (BaseInAndOutRule baseInAndOutRule : baseInAndOutRules) {
            searchBaseInAndOutRuleDet.setInAndOutRuleId(baseInAndOutRule.getInAndOutRuleId());
            List<BaseInAndOutRuleDet> baseInAndOutRuleDets = baseInAndOutRuleDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleDet));
            if (StringUtils.isNotEmpty(baseInAndOutRuleDets)){
                baseInAndOutRule.setBaseInAndOutRuleDets(baseInAndOutRuleDets);
            }
        }

        return baseInAndOutRules;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInAndOutRule baseInAndOutRule) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseInAndOutRule.class);
        example.createCriteria()
                .andEqualTo("inAndOutRuleName", baseInAndOutRule.getInAndOutRuleName())
                .andEqualTo("orgId", user.getOrganizationId());
        BaseInAndOutRule baseInAndOutRule1 = baseInAndOutRuleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInAndOutRule1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        example.createCriteria()
                .andEqualTo("orgId", user.getOrganizationId())
                .andEqualTo("warehouseId",baseInAndOutRule.getWarehouseId())
                .andEqualTo("category",baseInAndOutRule.getCategory());
        BaseInAndOutRule baseInAndOutRule2 = baseInAndOutRuleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInAndOutRule2)){
            throw new BizErrorException("该仓库已存在此类型的规则");
        }

        baseInAndOutRule.setCreateUserId(user.getUserId());
        baseInAndOutRule.setCreateTime(new Date());
        baseInAndOutRule.setModifiedUserId(user.getUserId());
        baseInAndOutRule.setModifiedTime(new Date());
        baseInAndOutRule.setStatus(StringUtils.isEmpty(baseInAndOutRule.getStatus())?1:baseInAndOutRule.getStatus());
        baseInAndOutRule.setOrgId(user.getOrganizationId());
        int i = baseInAndOutRuleMapper.insertUseGeneratedKeys(baseInAndOutRule);

        //履历
        BaseHtInAndOutRule baseHtInAndOutRule = new BaseHtInAndOutRule();
        BeanUtils.copyProperties(baseInAndOutRule, baseHtInAndOutRule);
        baseHtInAndOutRuleMapper.insertSelective(baseHtInAndOutRule);

        //明细
        List<BaseInAndOutRuleDet> baseInAndOutRuleDets = baseInAndOutRule.getBaseInAndOutRuleDets();
        if(StringUtils.isNotEmpty(baseInAndOutRuleDets)){
            int num = 1;
            for (BaseInAndOutRuleDet baseInAndOutRuleDet : baseInAndOutRuleDets) {
                baseInAndOutRuleDet.setInAndOutRuleId(baseInAndOutRule.getInAndOutRuleId());
                baseInAndOutRuleDet.setPriority(num++);//优先级自动递增
                baseInAndOutRuleDet.setCreateUserId(user.getUserId());
                baseInAndOutRuleDet.setCreateTime(new Date());
                baseInAndOutRuleDet.setModifiedUserId(user.getUserId());
                baseInAndOutRuleDet.setModifiedTime(new Date());
                baseInAndOutRuleDet.setStatus(StringUtils.isEmpty(baseInAndOutRuleDet.getStatus())?1:baseInAndOutRuleDet.getStatus());
                baseInAndOutRuleDet.setOrgId(user.getOrganizationId());
            }
            baseInAndOutRuleDetMapper.insertList(baseInAndOutRuleDets);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInAndOutRule baseInAndOutRule) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseInAndOutRule.class);
        example.createCriteria()
                .andEqualTo("orgId", user.getOrganizationId())
                .andEqualTo("inAndOutRuleName", baseInAndOutRule.getInAndOutRuleName())
                .andNotEqualTo("inAndOutRuleId",baseInAndOutRule.getInAndOutRuleId());
        BaseInAndOutRule baseInAndOutRule1 = baseInAndOutRuleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInAndOutRule1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        example.createCriteria()
                .andEqualTo("orgId", user.getOrganizationId())
                .andEqualTo("warehouseId",baseInAndOutRule.getWarehouseId())
                .andEqualTo("category",baseInAndOutRule.getCategory())
                .andNotEqualTo("inAndOutRuleId",baseInAndOutRule.getInAndOutRuleId());
        BaseInAndOutRule baseInAndOutRule2 = baseInAndOutRuleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseInAndOutRule2)){
            throw new BizErrorException("该仓库已存在此类型的规则");
        }

        baseInAndOutRule.setModifiedUserId(user.getUserId());
        baseInAndOutRule.setModifiedTime(new Date());
        baseInAndOutRule.setOrgId(user.getOrganizationId());
        int i=baseInAndOutRuleMapper.updateByPrimaryKeySelective(baseInAndOutRule);

        //履历
        BaseHtInAndOutRule baseHtInAndOutRule = new BaseHtInAndOutRule();
        BeanUtils.copyProperties(baseInAndOutRule, baseHtInAndOutRule);
        baseHtInAndOutRuleMapper.insertSelective(baseHtInAndOutRule);

        //删除原有明细
        Example example1 = new Example(BaseInAndOutRuleDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inAndOutRuleId", baseInAndOutRule.getInAndOutRuleId());
        baseInAndOutRuleDetMapper.deleteByExample(example1);

        //新增新的明细
        List<BaseInAndOutRuleDet> baseInAndOutRuleDets = baseInAndOutRule.getBaseInAndOutRuleDets();
        if(StringUtils.isNotEmpty(baseInAndOutRuleDets)){
            int num = 1;
            for (BaseInAndOutRuleDet baseInAndOutRuleDet : baseInAndOutRuleDets) {
                baseInAndOutRuleDet.setInAndOutRuleId(baseInAndOutRule.getInAndOutRuleId());
                baseInAndOutRuleDet.setPriority(num++);//优先级自动递增
                baseInAndOutRuleDet.setCreateUserId(user.getUserId());
                baseInAndOutRuleDet.setCreateTime(new Date());
                baseInAndOutRuleDet.setModifiedUserId(user.getUserId());
                baseInAndOutRuleDet.setModifiedTime(new Date());
                baseInAndOutRuleDet.setStatus(StringUtils.isEmpty(baseInAndOutRuleDet.getStatus())?1:baseInAndOutRuleDet.getStatus());
                baseInAndOutRuleDet.setOrgId(user.getOrganizationId());
            }
            baseInAndOutRuleDetMapper.insertList(baseInAndOutRuleDets);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtInAndOutRule> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInAndOutRule baseInAndOutRule = baseInAndOutRuleMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInAndOutRule)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInAndOutRule baseHtInAndOutRule = new BaseHtInAndOutRule();
            BeanUtils.copyProperties(baseInAndOutRule, baseHtInAndOutRule);
            list.add(baseHtInAndOutRule);

            //删除相关明细
            Example example1 = new Example(BaseInAndOutRuleDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inAndOutRuleId", baseInAndOutRule.getInAndOutRuleId());
            baseInAndOutRuleDetMapper.deleteByExample(example1);
        }

        baseHtInAndOutRuleMapper.insertList(list);

        return baseInAndOutRuleMapper.deleteByIds(ids);
    }
}
