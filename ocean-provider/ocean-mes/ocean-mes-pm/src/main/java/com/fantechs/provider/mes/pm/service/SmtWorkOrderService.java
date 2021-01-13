package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.dto.apply.SaveWorkOrderAndBom;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/13.
 */

public interface SmtWorkOrderService extends IService<SmtWorkOrder> {

    List<SmtWorkOrderDto> findList(SearchSmtWorkOrder searchSmtWorkOrder);

    List<SmtWorkOrderDto> pdaFindList(SearchSmtWorkOrder searchSmtWorkOrder);

    int saveWorkOrderDTO(SaveWorkOrderAndBom saveWorkOrderAndBom);

    //更新工单状态
    int updateWorkOrderStatus(Long workOrderId,int status);

    //工单完工产品
    int finishedProduct(Long workOrderId,Double count);
}