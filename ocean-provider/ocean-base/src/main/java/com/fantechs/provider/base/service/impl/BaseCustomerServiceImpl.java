package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseCustomer;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCustomer;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseCustomerMapper;
import com.fantechs.provider.base.service.BaseCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/27.
 */
@Service
public class BaseCustomerServiceImpl extends BaseService<BaseCustomer> implements BaseCustomerService {

    @Resource
    private BaseCustomerMapper baseCustomerMapper;

    @Override
    public List<BaseCustomer> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseCustomerMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseCustomer baseCustomer) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseCustomer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("customerCode", baseCustomer.getCustomerCode());
        List<BaseCustomer> baseCustomers = baseCustomerMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseCustomers)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseCustomer.setCreateUserId(currentUser.getUserId());
        baseCustomer.setCreateTime(new Date());
        baseCustomer.setModifiedUserId(currentUser.getUserId());
        baseCustomer.setModifiedTime(new Date());
        baseCustomer.setOrganizationId(currentUser.getOrganizationId());
        int i = baseCustomerMapper.insertSelective(baseCustomer);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] customerIds = ids.split(",");
        for (String customerId : customerIds) {
            BaseCustomer baseCustomer = baseCustomerMapper.selectByPrimaryKey(customerId);
            if(StringUtils.isEmpty(baseCustomer)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }

        return baseCustomerMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseCustomer baseCustomer) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseCustomer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("customerCode", baseCustomer.getCustomerCode());

        BaseCustomer customer = baseCustomerMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(customer)&&!customer.getCustomerId().equals(baseCustomer.getCustomerId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseCustomer.setModifiedUserId(currentUser.getUserId());
        baseCustomer.setModifiedTime(new Date());
        baseCustomer.setOrganizationId(currentUser.getOrganizationId());
        int i= baseCustomerMapper.updateByPrimaryKeySelective(baseCustomer);

        return i;
    }

    @Override
    public int saveByApi(BaseCustomer baseCustomer) {
        Example example = new Example(BaseCustomer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customerCode",baseCustomer.getCustomerCode());
        criteria.andEqualTo("organizationId",baseCustomer.getOrganizationId());
        BaseCustomer baseCustomerExist = baseCustomerMapper.selectOneByExample(example);
        int i= 0;
        if(StringUtils.isEmpty(baseCustomerExist)) {
            baseCustomer.setCreateTime(new Date());
            baseCustomer.setCreateUserId((long) 1);
            baseCustomer.setModifiedUserId((long) 1);
            baseCustomer.setModifiedTime(new Date());
            baseCustomer.setIsDelete((byte) 1);
            i = baseCustomerMapper.insertSelective(baseCustomer);
        }else{
            baseCustomer.setCustomerId(baseCustomerExist.getCustomerId());
            baseCustomer.setModifiedTime(new Date());
            baseCustomerMapper.updateByPrimaryKeySelective(baseCustomer);
        }
        return i;
    }
}
