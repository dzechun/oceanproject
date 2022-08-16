package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseCustomer;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCustomer;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseCustomerMapper extends MyMapper<BaseCustomer> {
    List<BaseCustomer> findList(Map<String, Object> map);
}