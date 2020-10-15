package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtHtWorkOrderService extends IService<SmtHtWorkOrder> {

    List<SmtHtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder);
}
