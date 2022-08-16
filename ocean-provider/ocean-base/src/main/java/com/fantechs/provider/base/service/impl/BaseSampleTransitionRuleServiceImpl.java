package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSampleTransitionRuleImport;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRule;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRuleDet;
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
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
        map.put("orgId", user.getOrganizationId());
        return baseSampleTransitionRuleMapper.findList(map);
    }

    @Override
    public int save(BaseSampleTransitionRule record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
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
        baseHtSampleTransitionRuleMapper.insertSelective(baseHtSampleTransitionRule);

        return i;
    }

    @Override
    public int update(BaseSampleTransitionRule entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());

        BaseHtSampleTransitionRule baseHtSampleTransitionRule = new BaseHtSampleTransitionRule();
        BeanUtils.copyProperties(entity,baseHtSampleTransitionRule);
        baseHtSampleTransitionRuleMapper.insertSelective(baseHtSampleTransitionRule);

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
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseSampleTransitionRule.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("sampleTransitionRuleCode",entity.getSampleTransitionRuleCode());
        if (StringUtils.isNotEmpty(entity.getSampleTransitionRuleId())){
            criteria.andNotEqualTo("sampleTransitionRuleId",entity.getSampleTransitionRuleId());
        }
        BaseSampleTransitionRule baseSampleTransitionRule = baseSampleTransitionRuleMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSampleTransitionRule)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSampleTransitionRuleImport> baseSampleTransitionRuleImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseSampleTransitionRule> list = new LinkedList<>();
        LinkedList<BaseHtSampleTransitionRule> htList = new LinkedList<>();
        LinkedList<BaseSampleTransitionRuleImport> sampleTransitionRuleImports = new LinkedList<>();
        for (int i = 0; i < baseSampleTransitionRuleImports.size(); i++) {
            BaseSampleTransitionRuleImport baseSampleTransitionRuleImport = baseSampleTransitionRuleImports.get(i);
            String sampleTransitionRuleCode = baseSampleTransitionRuleImport.getSampleTransitionRuleCode();

            if (StringUtils.isEmpty(
                    sampleTransitionRuleCode
            )){
                fail.add(i+4);
                continue;
            }

            sampleTransitionRuleImports.add(baseSampleTransitionRuleImport);
        }

        if (StringUtils.isNotEmpty(sampleTransitionRuleImports)){
            //对合格数据进行分组
            HashMap<String, List<BaseSampleTransitionRuleImport>> map = sampleTransitionRuleImports.stream().collect(Collectors.groupingBy(BaseSampleTransitionRuleImport::getSampleTransitionRuleCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<BaseSampleTransitionRuleImport> baseSampleTransitionRuleImports1 = map.get(code);
                //新增父级数据
                BaseSampleTransitionRule baseSampleTransitionRule = new BaseSampleTransitionRule();
                BeanUtils.copyProperties(baseSampleTransitionRuleImports1.get(0), baseSampleTransitionRule);
                baseSampleTransitionRule.setCreateTime(new Date());
                baseSampleTransitionRule.setCreateUserId(currentUser.getUserId());
                baseSampleTransitionRule.setModifiedUserId(currentUser.getUserId());
                baseSampleTransitionRule.setModifiedTime(new Date());
                baseSampleTransitionRule.setOrgId(currentUser.getOrganizationId());
                baseSampleTransitionRule.setStatus((byte) 1);
                success += baseSampleTransitionRuleMapper.insertUseGeneratedKeys(baseSampleTransitionRule);

                BaseHtSampleTransitionRule baseHtSampleTransitionRule = new BaseHtSampleTransitionRule();
                BeanUtils.copyProperties(baseSampleTransitionRule, baseHtSampleTransitionRule);
                htList.add(baseHtSampleTransitionRule);

                //新增明细数据
                LinkedList<BaseSampleTransitionRuleDet> detList = new LinkedList<>();
                for (BaseSampleTransitionRuleImport baseSampleTransitionRuleImport : baseSampleTransitionRuleImports1) {
                    BaseSampleTransitionRuleDet baseSampleTransitionRuleDet = new BaseSampleTransitionRuleDet();
                    BeanUtils.copyProperties(baseSampleTransitionRuleImport, baseSampleTransitionRuleDet);
                    baseSampleTransitionRuleDet.setSampleTransitionRuleId(baseSampleTransitionRule.getSampleTransitionRuleId());
                    baseSampleTransitionRuleDet.setStatus((byte) 1);
                    baseSampleTransitionRuleDet.setIfInitialPhase(StringUtils.isEmpty(baseSampleTransitionRuleImport.getIfInitialPhase())?1:baseSampleTransitionRuleImport.getIfInitialPhase().byteValue());
                    baseSampleTransitionRuleDet.setRigorStage(StringUtils.isEmpty(baseSampleTransitionRuleImport.getRigorStage())?1:baseSampleTransitionRuleImport.getRigorStage().byteValue());
                    baseSampleTransitionRuleDet.setContinuousBatchCondition(StringUtils.isEmpty(baseSampleTransitionRuleImport.getContinuousBatchCondition())?1:baseSampleTransitionRuleImport.getContinuousBatchCondition().byteValue());
                    detList.add(baseSampleTransitionRuleDet);
                }
                baseSampleTransitionRuleDetMapper.insertList(detList);
            }
            baseHtSampleTransitionRuleMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
