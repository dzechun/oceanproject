package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseCurrencyDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseCurrencyImport;
import com.fantechs.common.base.general.entity.basic.BaseCurrency;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseCurrencyMapper;
import com.fantechs.provider.base.service.BaseCurrencyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

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
        Example example = new Example(BaseCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("currencyCode", baseCurrency.getCurrencyCode());
        List<BaseCurrency> smtCurrencies = baseCurrencyMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtCurrencies)) {
            throw new BizErrorException("编码已存在");
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

        Example example = new Example(BaseCurrency.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("currencyCode", baseCurrency.getCurrencyCode());
        criteria1.andNotEqualTo("currencyId", baseCurrency.getCurrencyId());
        example.and(criteria1);
        List<BaseCurrency> smtCurrencies = baseCurrencyMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtCurrencies)) {
            throw new BizErrorException("编码已存在");
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
        map.put("orgId", user.getOrganizationId());
        return baseCurrencyMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseCurrencyImport> baseCurrencyImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseCurrency> list = new LinkedList<>();

        for (int i = 0; i < baseCurrencyImports.size(); i++) {
            BaseCurrencyImport baseCurrencyImport = baseCurrencyImports.get(i);
            String currencyCode = baseCurrencyImport.getCurrencyCode();
            String currencyName = baseCurrencyImport.getCurrencyName();
            if (StringUtils.isEmpty(
                    currencyCode,currencyName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseCurrency.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("currencyCode",currencyCode);
            if (StringUtils.isNotEmpty(baseCurrencyMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (BaseCurrency baseCurrency : list) {
                    if (baseCurrency.getCurrencyCode().equals(currencyCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            BaseCurrency baseCurrency = new BaseCurrency();
            BeanUtils.copyProperties(baseCurrencyImport, baseCurrency);
            baseCurrency.setCreateTime(new Date());
            baseCurrency.setCreateUserId(currentUser.getUserId());
            baseCurrency.setModifiedTime(new Date());
            baseCurrency.setModifiedUserId(currentUser.getUserId());
            baseCurrency.setStatus(StringUtils.isEmpty(baseCurrencyImport.getStatus())?1:baseCurrencyImport.getStatus().byteValue());
            baseCurrency.setOrganizationId(currentUser.getOrganizationId());
            list.add(baseCurrency);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseCurrencyMapper.insertList(list);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
