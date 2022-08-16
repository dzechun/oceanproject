package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRuleSet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleSetMapper;
import com.fantechs.provider.base.mapper.BaseHtBarcodeRuleSetMapper;
import com.fantechs.provider.base.service.BaseBarcodeRuleSetService;
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
 * Created by wcz on 2020/11/09.
 */
@Service
public class BaseBarcodeRuleSetServiceImpl extends BaseService<BaseBarcodeRuleSet> implements BaseBarcodeRuleSetService {

        @Resource
        private BaseBarcodeRuleSetMapper baseBarcodeRuleSetMapper;
        @Resource
        private BaseHtBarcodeRuleSetMapper baseHtBarcodeRuleSetMapper;

        @Override
        public List<BaseBarcodeRuleSetDto> findList(Map<String, Object> map) {
            if(StringUtils.isEmpty(map.get("organizationId"))) {
                SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
                map.put("organizationId", user.getOrganizationId());
            }
            return baseBarcodeRuleSetMapper.findList(map);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(BaseBarcodeRuleSet baseBarcodeRuleSet) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

            Example example = new Example(BaseBarcodeRuleSet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("barcodeRuleSetCode", baseBarcodeRuleSet.getBarcodeRuleSetCode());
            List<BaseBarcodeRuleSet> baseBarcodeRuleSets = baseBarcodeRuleSetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseBarcodeRuleSets)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            baseBarcodeRuleSet.setCreateUserId(currentUser.getUserId());
            baseBarcodeRuleSet.setCreateTime(new Date());
            baseBarcodeRuleSet.setModifiedUserId(currentUser.getUserId());
            baseBarcodeRuleSet.setModifiedTime(new Date());
            baseBarcodeRuleSet.setOrganizationId(currentUser.getOrganizationId());
            int i = baseBarcodeRuleSetMapper.insertUseGeneratedKeys(baseBarcodeRuleSet);

            //新增工单BOM历史信息
            BaseHtBarcodeRuleSet baseHtBarcodeRuleSet =new BaseHtBarcodeRuleSet();
            BeanUtils.copyProperties(baseBarcodeRuleSet, baseHtBarcodeRuleSet);
            baseHtBarcodeRuleSetMapper.insertSelective(baseHtBarcodeRuleSet);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(BaseBarcodeRuleSet baseBarcodeRuleSet) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

            Example example = new Example(BaseBarcodeRuleSet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("barcodeRuleSetCode", baseBarcodeRuleSet.getBarcodeRuleSetCode());
            BaseBarcodeRuleSet ruleSet = baseBarcodeRuleSetMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(ruleSet)&&!ruleSet.getBarcodeRuleSetId().equals(baseBarcodeRuleSet.getBarcodeRuleSetId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            baseBarcodeRuleSet.setModifiedUserId(currentUser.getUserId());
            baseBarcodeRuleSet.setModifiedTime(new Date());
            baseBarcodeRuleSet.setOrganizationId(currentUser.getOrganizationId());
            int i = baseBarcodeRuleSetMapper.updateByPrimaryKeySelective(baseBarcodeRuleSet);

            //新增工单BOM历史信息
            BaseHtBarcodeRuleSet baseHtBarcodeRuleSet =new BaseHtBarcodeRuleSet();
            BeanUtils.copyProperties(baseBarcodeRuleSet, baseHtBarcodeRuleSet);
            baseHtBarcodeRuleSetMapper.insertSelective(baseHtBarcodeRuleSet);

            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<BaseHtBarcodeRuleSet> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

            String[] idArr = ids.split(",");
            for (String id : idArr) {
                BaseBarcodeRuleSet baseBarcodeRuleSet = baseBarcodeRuleSetMapper.selectByPrimaryKey(id);
                if(StringUtils.isEmpty(baseBarcodeRuleSet)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //新增条码规则集合历史信息
                BaseHtBarcodeRuleSet baseHtBarcodeRuleSet =new BaseHtBarcodeRuleSet();
                BeanUtils.copyProperties(baseBarcodeRuleSet, baseHtBarcodeRuleSet);
                list.add(baseHtBarcodeRuleSet);
            }
            baseHtBarcodeRuleSetMapper.insertList(list);

            return baseBarcodeRuleSetMapper.deleteByIds(ids);
        }
}
