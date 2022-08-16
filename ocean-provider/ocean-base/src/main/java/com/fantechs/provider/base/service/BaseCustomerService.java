package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseCustomer;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCustomer;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/27.
 */

public interface BaseCustomerService extends IService<BaseCustomer> {

    List<BaseCustomer> findList(Map<String, Object> map);

    int saveByApi(BaseCustomer baseCustomer);
}
