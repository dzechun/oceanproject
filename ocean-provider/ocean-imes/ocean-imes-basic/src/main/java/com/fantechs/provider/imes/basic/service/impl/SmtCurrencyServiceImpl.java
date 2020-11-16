package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtCurrencyDto;
import com.fantechs.common.base.entity.basic.SmtCurrency;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtCurrencyMapper;
import com.fantechs.provider.imes.basic.service.SmtCurrencyService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/13.
 */
@Service
public class SmtCurrencyServiceImpl extends BaseService<SmtCurrency> implements SmtCurrencyService {

    @Resource
    private SmtCurrencyMapper smtCurrencyMapper;

    @Override
    public int save(SmtCurrency smtCurrency) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("currencyCode", smtCurrency.getCurrencyCode())
                .orEqualTo("currencyName", smtCurrency.getCurrencyName());
        List<SmtCurrency> smtCurrencies = smtCurrencyMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtCurrencies)) {
            throw new BizErrorException("编码或名称已存在");
        }
        smtCurrency.setCreateUserId(currentUser.getUserId());
        smtCurrency.setCreateTime(new Date());
        smtCurrency.setModifiedTime(new Date());
        smtCurrency.setModifiedUserId(currentUser.getUserId());
        smtCurrency.setStatus(StringUtils.isEmpty(smtCurrency.getStatus())?1:smtCurrency.getStatus());
        return smtCurrencyMapper.insertUseGeneratedKeys(smtCurrency);
    }

    @Override
    public int update(SmtCurrency smtCurrency) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("currencyCode", smtCurrency.getCurrencyCode())
                .orEqualTo("currencyName", smtCurrency.getCurrencyName());
        criteria1.andNotEqualTo("currencyId", smtCurrency.getCurrencyId());
        example.and(criteria1);
        List<SmtCurrency> smtCurrencies = smtCurrencyMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtCurrencies)) {
            throw new BizErrorException("编码或名称已存在");
        }

        smtCurrency.setModifiedUserId(currentUser.getUserId());
        smtCurrency.setModifiedTime(new Date());
        return smtCurrencyMapper.updateByPrimaryKeySelective(smtCurrency);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            SmtCurrency smtCurrency = smtCurrencyMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtCurrency)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtCurrencyMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtCurrencyDto> findList(Map<String, Object> map) {
        return smtCurrencyMapper.findList(map);
    }
}
