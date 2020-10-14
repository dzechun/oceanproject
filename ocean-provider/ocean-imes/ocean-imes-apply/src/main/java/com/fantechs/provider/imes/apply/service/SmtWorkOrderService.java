package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/13.
 */

public interface SmtWorkOrderService extends IService<SmtWorkOrder> {

    List<SmtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder);
}
