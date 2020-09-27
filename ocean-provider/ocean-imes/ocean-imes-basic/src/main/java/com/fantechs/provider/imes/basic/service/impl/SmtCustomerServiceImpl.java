package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtCustomer;
import com.fantechs.common.base.entity.basic.search.SearchSmtCustomer;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtCustomerMapper;
import com.fantechs.provider.imes.basic.service.SmtCustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
