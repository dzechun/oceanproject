package com.fantechs.provider.mes.pm.service;


import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/13.
 */

public interface MesPmWorkOrderService extends IService<MesPmWorkOrder> {

    List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder);

    List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder);

    int updateInventoryQty(MesPmWorkOrder mesPmWorkOrder);

    MesPmWorkOrder saveByApi(MesPmWorkOrder mesPmWorkOrder);

    List<MesPmWorkOrder> getWorkOrderList(List<String> workOrderIds);

    int batchUpdate(List<MesPmWorkOrder> mesPmWorkOrders);

    int save(MesPmWorkOrderDto mesPmWorkOrderDto);

    int update(MesPmWorkOrderDto mesPmWorkOrderDto);

    int updatePmWorkOrder(MesPmWorkOrder mesPmWorkOrder);

    int inPushDown(List<MesPmWorkOrder> mesPmWorkOrders);

    int outPushDown(List<MesPmWorkOrderBomDto> mesPmWorkOrderBomDtos);

    int outPushDownDailyPlan(List<MesPmWorkOrderDto> mesPmWorkOrderDtos);
}
