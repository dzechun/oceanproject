package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtCustomer;
import com.fantechs.common.base.entity.basic.search.SearchSmtCustomer;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/27.
 */

public interface SmtCustomerService extends IService<SmtCustomer> {

    List<SmtCustomer> findList(SearchSmtCustomer searchSmtCustomer);
}
