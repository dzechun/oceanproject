package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */

public interface MesPmHtWorkOrderService extends IService<MesPmHtWorkOrder> {

    List<MesPmHtWorkOrder> findList(SearchMesPmWorkOrder searchMesPmWorkOrder);
}
