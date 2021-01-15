package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface SmtHtWorkOrderService extends IService<SmtHtWorkOrder> {

    List<SmtHtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder);
}
