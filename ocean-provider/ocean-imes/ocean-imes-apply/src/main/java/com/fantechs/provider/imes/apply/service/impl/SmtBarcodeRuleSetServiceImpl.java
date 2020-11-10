package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtBarcodeRuleSetMapper;
import com.fantechs.provider.imes.apply.mapper.SmtHtBarcodeRuleSetMapper;
import com.fantechs.provider.imes.apply.service.SmtBarcodeRuleSetService;
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
 * Created by wcz on 2020/11/09.
 */
@Service
public class SmtBarcodeRuleSetServiceImpl extends BaseService<SmtBarcodeRuleSet> implements SmtBarcodeRuleSetService {

        @Resource
        private SmtBarcodeRuleSetMapper smtBarcodeRuleSetMapper;
        @Resource
        private SmtHtBarcodeRuleSetMapper smtHtBarcodeRuleSetMapper;

        @Override
        public List<SmtBarcodeRuleSetDto> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet) {
            return smtBarcodeRuleSetMapper.findList(searchSmtBarcodeRuleSet);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtBarcodeRuleSet smtBarcodeRuleSet) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtBarcodeRuleSet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleSetCode",smtBarcodeRuleSet.getBarcodeRuleSetCode());
            List<SmtBarcodeRuleSet> smtBarcodeRuleSets = smtBarcodeRuleSetMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtBarcodeRuleSets)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            smtBarcodeRuleSet.setCreateUserId(currentUser.getUserId());
            smtBarcodeRuleSet.setCreateTime(new Date());
            smtBarcodeRuleSet.setModifiedUserId(currentUser.getUserId());
            smtBarcodeRuleSet.setModifiedTime(new Date());
            int i = smtBarcodeRuleSetMapper.insertUseGeneratedKeys(smtBarcodeRuleSet);

            //新增工单BOM历史信息
            SmtHtBarcodeRuleSet smtHtBarcodeRuleSet=new SmtHtBarcodeRuleSet();
            BeanUtils.copyProperties(smtBarcodeRuleSet,smtHtBarcodeRuleSet);
            smtHtBarcodeRuleSetMapper.insertSelective(smtHtBarcodeRuleSet);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtBarcodeRuleSet smtBarcodeRuleSet) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtBarcodeRuleSet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("barcodeRuleSetCode",smtBarcodeRuleSet.getBarcodeRuleSetCode());
            SmtBarcodeRuleSet ruleSet = smtBarcodeRuleSetMapper.selectOneByExample(example);
            if(StringUtils.isNotEmpty(ruleSet)&&!ruleSet.getBarcodeRuleSetId().equals(smtBarcodeRuleSet.getBarcodeRuleSetId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            smtBarcodeRuleSet.setModifiedUserId(currentUser.getUserId());
            smtBarcodeRuleSet.setModifiedTime(new Date());
            int i = smtBarcodeRuleSetMapper.updateByPrimaryKeySelective(smtBarcodeRuleSet);

            //新增工单BOM历史信息
            SmtHtBarcodeRuleSet smtHtBarcodeRuleSet=new SmtHtBarcodeRuleSet();
            BeanUtils.copyProperties(smtBarcodeRuleSet,smtHtBarcodeRuleSet);
            smtHtBarcodeRuleSetMapper.insertSelective(smtHtBarcodeRuleSet);

            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<SmtHtBarcodeRuleSet> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            String[] idArr = ids.split(",");
            for (String id : idArr) {
                SmtBarcodeRuleSet smtBarcodeRuleSet = smtBarcodeRuleSetMapper.selectByPrimaryKey(id);
                if(StringUtils.isEmpty(smtBarcodeRuleSet)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //新增条码规则集合历史信息
                SmtHtBarcodeRuleSet smtHtBarcodeRuleSet=new SmtHtBarcodeRuleSet();
                BeanUtils.copyProperties(smtBarcodeRuleSet,smtHtBarcodeRuleSet);
                list.add(smtHtBarcodeRuleSet);
            }
            smtHtBarcodeRuleSetMapper.insertList(list);

            return smtBarcodeRuleSetMapper.deleteByIds(ids);
        }
}
