package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseCurrencyDto;
import com.fantechs.common.base.general.entity.basic.BaseCurrency;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseCurrencyMapper;
import com.fantechs.provider.base.service.BaseCurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/13.
 */
@Service
public class BaseCurrencyServiceImpl extends BaseService<BaseCurrency> implements BaseCurrencyService {

    @Resource
    private BaseCurrencyMapper baseCurrencyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseCurrency baseCurrency) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("currencyCode", baseCurrency.getCurrencyCode())
                .orEqualTo("currencyName", baseCurrency.getCurrencyName());
        List<BaseCurrency> smtCurrencies = baseCurrencyMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtCurrencies)) {
            throw new BizErrorException("编码或名称已存在");
        }
        baseCurrency.setCreateUserId(currentUser.getUserId());
        baseCurrency.setCreateTime(new Date());
        baseCurrency.setModifiedTime(new Date());
        baseCurrency.setModifiedUserId(currentUser.getUserId());
        baseCurrency.setStatus(StringUtils.isEmpty(baseCurrency.getStatus())?1: baseCurrency.getStatus());
        baseCurrency.setOrganizationId(currentUser.getOrganizationId());
        return baseCurrencyMapper.insertUseGeneratedKeys(baseCurrency);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseCurrency baseCurrency) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("currencyCode", baseCurrency.getCurrencyCode())
                .orEqualTo("currencyName", baseCurrency.getCurrencyName());
        criteria1.andNotEqualTo("currencyId", baseCurrency.getCurrencyId());
        example.and(criteria1);
        List<BaseCurrency> smtCurrencies = baseCurrencyMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtCurrencies)) {
            throw new BizErrorException("编码或名称已存在");
        }

        baseCurrency.setModifiedUserId(currentUser.getUserId());
        baseCurrency.setModifiedTime(new Date());
        baseCurrency.setOrganizationId(currentUser.getOrganizationId());
        return baseCurrencyMapper.updateByPrimaryKeySelective(baseCurrency);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseCurrency baseCurrency = baseCurrencyMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseCurrency)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return baseCurrencyMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseCurrencyDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseCurrencyMapper.findList(map);
    }
}
