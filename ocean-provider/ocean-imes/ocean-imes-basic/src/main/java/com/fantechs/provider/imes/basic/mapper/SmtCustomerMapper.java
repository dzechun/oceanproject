package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtCustomer;
import com.fantechs.common.base.entity.basic.search.SearchSmtCustomer;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtCustomerMapper extends MyMapper<SmtCustomer> {
    List<SmtCustomer> findList(SearchSmtCustomer searchSmtCustomer);
}