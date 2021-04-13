package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SaveWorkOrderAndBom;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/13.
 */

public interface MesPmWorkOrderService extends IService<MesPmWorkOrder> {

    List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder);

    List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder);

    int saveWorkOrderDTO(SaveWorkOrderAndBom saveWorkOrderAndBom);

    //更新工单状态
    int updateWorkOrderStatus(Long workOrderId,int status);

    //工单完工产品
    int finishedProduct(Long workOrderId,Double count);
}
