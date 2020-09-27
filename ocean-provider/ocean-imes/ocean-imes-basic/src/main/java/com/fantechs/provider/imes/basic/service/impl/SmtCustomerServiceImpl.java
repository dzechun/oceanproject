package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtCustomer;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.history.SmtHtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtCustomer;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtCustomerMapper;
import com.fantechs.provider.imes.basic.service.SmtCustomerService;
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
 * Created by wcz on 2020/09/27.
 */
@Service
public class SmtCustomerServiceImpl  extends BaseService<SmtCustomer> implements SmtCustomerService {

    @Resource
    private SmtCustomerMapper smtCustomerMapper;

    @Override
    public List<SmtCustomer> findList(SearchSmtCustomer searchSmtCustomer) {
        return smtCustomerMapper.findList(searchSmtCustomer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtCustomer smtCustomer) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtCustomer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customerCode",smtCustomer.getCustomerCode());
        List<SmtCustomer> smtCustomers = smtCustomerMapper.selectByExample(example);
        if(null!=smtCustomers&&smtCustomers.size()>0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtCustomer.setCreateUserId(currentUser.getUserId());
        smtCustomer.setCreateTime(new Date());
        smtCustomer.setModifiedUserId(currentUser.getUserId());
        smtCustomer.setModifiedTime(new Date());
        int i = smtCustomerMapper.insertSelective(smtCustomer);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] customerIds = ids.split(",");
        for (String customerId : customerIds) {
            SmtCustomer smtCustomer = smtCustomerMapper.selectByPrimaryKey(customerId);
            if(StringUtils.isEmpty(smtCustomer)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return smtCustomerMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtCustomer smtCustomer) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtCustomer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customerCode",smtCustomer.getCustomerCode());

        SmtCustomer customer = smtCustomerMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(customer)&&!customer.getCustomerId().equals(smtCustomer.getCustomerId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtCustomer.setModifiedUserId(currentUser.getUserId());
        smtCustomer.setModifiedTime(new Date());
        int i= smtCustomerMapper.updateByPrimaryKeySelective(smtCustomer);

        return i;
    }
}
