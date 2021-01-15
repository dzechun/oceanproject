package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSetDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.SmtBarcodeRuleSetDetMapper;
import com.fantechs.provider.mes.pm.service.SmtBarcodeRuleSetDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/10.
 */
@Service
public class SmtBarcodeRuleSetDetServiceImpl extends BaseService<SmtBarcodeRuleSetDet> implements SmtBarcodeRuleSetDetService {

        @Resource
        private SmtBarcodeRuleSetDetMapper smtBarcodeRuleSetDetMapper;

        @Override
        public List<SmtBarcodeRuleSetDetDto> findList(SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet) {
            return smtBarcodeRuleSetDetMapper.findList(searchSmtBarcodeRuleSetDet);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int bindBarcodeRule(Long barcodeRuleSetId, List<Long> barcodeRuleIds) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
                if(StringUtils.isEmpty(currentUser)){
                        throw new BizErrorException(ErrorCodeEnum.UAC10011039);
                }
                List<SmtBarcodeRuleSetDet> list=new ArrayList<>();
                Example example = new Example(SmtBarcodeRuleSetDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("barcodeRuleSetId",barcodeRuleSetId);
                smtBarcodeRuleSetDetMapper.deleteByExample(example);

                for (Long barcodeRuleId : barcodeRuleIds) {
                      SmtBarcodeRuleSetDet smtBarcodeRuleSetDet = new SmtBarcodeRuleSetDet();
                      smtBarcodeRuleSetDet.setBarcodeRuleSetId(barcodeRuleSetId);
                      smtBarcodeRuleSetDet.setBarcodeRuleId(barcodeRuleId);
                      smtBarcodeRuleSetDet.setIsDelete((byte) 1);
                      smtBarcodeRuleSetDet.setCreateUserId(currentUser.getUserId());
                      smtBarcodeRuleSetDet.setCreateTime(new Date());
                      smtBarcodeRuleSetDet.setModifiedUserId(currentUser.getUserId());
                      smtBarcodeRuleSetDet.setModifiedTime(new Date());
                      list.add(smtBarcodeRuleSetDet);
                }
                return smtBarcodeRuleSetDetMapper.insertList(list);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtBarcodeRuleSetDet smtBarcodeRuleSetDet) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
                if(StringUtils.isEmpty(currentUser)){
                        throw new BizErrorException(ErrorCodeEnum.UAC10011039);
                }

                Example example = new Example(SmtBarcodeRuleSetDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("barcodeRuleSetId",smtBarcodeRuleSetDet.getBarcodeRuleSetId());
                criteria.andEqualTo("barcodeRuleId",smtBarcodeRuleSetDet.getBarcodeRuleId());

                List<SmtBarcodeRuleSetDet> smtBarcodeRuleSetDets = smtBarcodeRuleSetDetMapper.selectByExample(example);
                if(StringUtils.isNotEmpty(smtBarcodeRuleSetDets)){
                        throw new BizErrorException("该条码集合下的条码规则已存在");
                }


                smtBarcodeRuleSetDet.setCreateUserId(currentUser.getUserId());
                smtBarcodeRuleSetDet.setCreateTime(new Date());
                smtBarcodeRuleSetDet.setModifiedUserId(currentUser.getUserId());
                smtBarcodeRuleSetDet.setModifiedTime(new Date());
                return smtBarcodeRuleSetDetMapper.insertUseGeneratedKeys(smtBarcodeRuleSetDet);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtBarcodeRuleSetDet smtBarcodeRuleSetDet) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
                if(StringUtils.isEmpty(currentUser)){
                        throw new BizErrorException(ErrorCodeEnum.UAC10011039);
                }

                Example example = new Example(SmtBarcodeRuleSetDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("barcodeRuleSetId",smtBarcodeRuleSetDet.getBarcodeRuleSetId());
                criteria.andEqualTo("barcodeRuleId",smtBarcodeRuleSetDet.getBarcodeRuleId());
                SmtBarcodeRuleSetDet ruleSetDet = smtBarcodeRuleSetDetMapper.selectOneByExample(example);

                if(StringUtils.isNotEmpty(ruleSetDet)&&!ruleSetDet.getBarcodeRuleSetDetId().equals(smtBarcodeRuleSetDet.getBarcodeRuleSetDetId())){
                        throw new BizErrorException("该条码集合下的条码规则已存在");
                }

                smtBarcodeRuleSetDet.setModifiedUserId(currentUser.getUserId());
                smtBarcodeRuleSetDet.setModifiedTime(new Date());
                return smtBarcodeRuleSetDetMapper.updateByPrimaryKeySelective(smtBarcodeRuleSetDet);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
                if(StringUtils.isEmpty(currentUser)){
                        throw new BizErrorException(ErrorCodeEnum.UAC10011039);
                }

                String[] barcodeRuleSetDetIds = ids.split(",");
                for (String barcodeRuleSetDetId : barcodeRuleSetDetIds) {
                        SmtBarcodeRuleSetDet smtBarcodeRuleSetDet = smtBarcodeRuleSetDetMapper.selectByPrimaryKey(barcodeRuleSetDetId);
                        if(StringUtils.isEmpty(smtBarcodeRuleSetDet)){
                                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                }
                return smtBarcodeRuleSetDetMapper.deleteByIds(ids);
        }
}
