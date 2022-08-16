package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSetDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSetDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleMapper;
import com.fantechs.provider.base.mapper.BaseBarcodeRuleSetDetMapper;
import com.fantechs.provider.base.service.BaseBarcodeRuleSetDetService;
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
 * Created by wcz on 2020/11/10.
 */
@Service
public class BaseBarcodeRuleSetDetServiceImpl extends BaseService<BaseBarcodeRuleSetDet> implements BaseBarcodeRuleSetDetService {

        @Resource
        private BaseBarcodeRuleSetDetMapper baseBarcodeRuleSetDetMapper;
        @Resource
        private BaseBarcodeRuleMapper baseBarcodeRuleMapper;

        @Override
        public List<BaseBarcodeRuleSetDetDto> findList(Map<String, Object> map) {
                SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
                map.put("orgId", user.getOrganizationId());
                return baseBarcodeRuleSetDetMapper.findList(map);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int bindBarcodeRule(Long barcodeRuleSetId, List<Long> barcodeRuleIds) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

                if(barcodeRuleIds.size()<1){
                        throw new BizErrorException("绑定条码规则为空");
                }
                List<BaseBarcodeRuleSetDet> list=new ArrayList<>();
                Example example = new Example(BaseBarcodeRuleSetDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("barcodeRuleSetId",barcodeRuleSetId);
                baseBarcodeRuleSetDetMapper.deleteByExample(example);

                SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
                for (Long barcodeRuleId : barcodeRuleIds) {
                        searchBaseBarcodeRule.setBarcodeRuleId(barcodeRuleId);
                        List<BaseBarcodeRuleDto> baseBarcodeRuleDtos = baseBarcodeRuleMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRule));
                        if (StringUtils.isEmpty(baseBarcodeRuleDtos))
                                throw new BizErrorException("条码规则不存在");
                        for (BaseBarcodeRuleSetDet baseBarcodeRuleSetDet : list){
                                searchBaseBarcodeRule.setBarcodeRuleId(baseBarcodeRuleSetDet.getBarcodeRuleId());
                                List<BaseBarcodeRuleDto> baseBarcodeRuleDtoList = baseBarcodeRuleMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRule));
                                if (StringUtils.isEmpty(baseBarcodeRuleDtoList))
                                        throw new BizErrorException("条码规则不存在");
                                if (baseBarcodeRuleDtos.get(0).getLabelCategoryId() == baseBarcodeRuleDtoList.get(0).getLabelCategoryId())
                                        throw new BizErrorException("重复的条码规则类型");
                        }

                        BaseBarcodeRuleSetDet baseBarcodeRuleSetDet = new BaseBarcodeRuleSetDet();
                        baseBarcodeRuleSetDet.setBarcodeRuleSetId(barcodeRuleSetId);
                        baseBarcodeRuleSetDet.setBarcodeRuleId(barcodeRuleId);
                        baseBarcodeRuleSetDet.setIsDelete((byte) 1);
                        baseBarcodeRuleSetDet.setCreateUserId(currentUser.getUserId());
                        baseBarcodeRuleSetDet.setCreateTime(new Date());
                        baseBarcodeRuleSetDet.setModifiedUserId(currentUser.getUserId());
                        baseBarcodeRuleSetDet.setModifiedTime(new Date());
                        baseBarcodeRuleSetDet.setOrganizationId(currentUser.getOrganizationId());
                        list.add(baseBarcodeRuleSetDet);
                }
                return baseBarcodeRuleSetDetMapper.insertList(list);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(BaseBarcodeRuleSetDet baseBarcodeRuleSetDet) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

                Example example = new Example(BaseBarcodeRuleSetDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("barcodeRuleSetId", baseBarcodeRuleSetDet.getBarcodeRuleSetId());
                criteria.andEqualTo("barcodeRuleId", baseBarcodeRuleSetDet.getBarcodeRuleId());

                List<BaseBarcodeRuleSetDet> baseBarcodeRuleSetDets = baseBarcodeRuleSetDetMapper.selectByExample(example);
                if(StringUtils.isNotEmpty(baseBarcodeRuleSetDets)){
                        throw new BizErrorException("该条码集合下的条码规则已存在");
                }


                baseBarcodeRuleSetDet.setCreateUserId(currentUser.getUserId());
                baseBarcodeRuleSetDet.setCreateTime(new Date());
                baseBarcodeRuleSetDet.setModifiedUserId(currentUser.getUserId());
                baseBarcodeRuleSetDet.setModifiedTime(new Date());
                baseBarcodeRuleSetDet.setOrganizationId(currentUser.getOrganizationId());
                return baseBarcodeRuleSetDetMapper.insertUseGeneratedKeys(baseBarcodeRuleSetDet);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(BaseBarcodeRuleSetDet baseBarcodeRuleSetDet) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

                Example example = new Example(BaseBarcodeRuleSetDet.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("barcodeRuleSetId", baseBarcodeRuleSetDet.getBarcodeRuleSetId());
                criteria.andEqualTo("barcodeRuleId", baseBarcodeRuleSetDet.getBarcodeRuleId());
                BaseBarcodeRuleSetDet ruleSetDet = baseBarcodeRuleSetDetMapper.selectOneByExample(example);

                if(StringUtils.isNotEmpty(ruleSetDet)&&!ruleSetDet.getBarcodeRuleSetDetId().equals(baseBarcodeRuleSetDet.getBarcodeRuleSetDetId())){
                        throw new BizErrorException("该条码集合下的条码规则已存在");
                }

                baseBarcodeRuleSetDet.setModifiedUserId(currentUser.getUserId());
                baseBarcodeRuleSetDet.setModifiedTime(new Date());
                baseBarcodeRuleSetDet.setOrganizationId(currentUser.getOrganizationId());
                return baseBarcodeRuleSetDetMapper.updateByPrimaryKeySelective(baseBarcodeRuleSetDet);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
                SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

                String[] barcodeRuleSetDetIds = ids.split(",");
                for (String barcodeRuleSetDetId : barcodeRuleSetDetIds) {
                        BaseBarcodeRuleSetDet baseBarcodeRuleSetDet = baseBarcodeRuleSetDetMapper.selectByPrimaryKey(barcodeRuleSetDetId);
                        if(StringUtils.isEmpty(baseBarcodeRuleSetDet)){
                                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                        }
                }
                return baseBarcodeRuleSetDetMapper.deleteByIds(ids);
        }
}
