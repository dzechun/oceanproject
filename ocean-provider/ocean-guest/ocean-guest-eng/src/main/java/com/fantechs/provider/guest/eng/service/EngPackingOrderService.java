package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface EngPackingOrderService extends IService<EngPackingOrder> {
    List<EngPackingOrderDto> findList(Map<String, Object> map);
    int censor(EngPackingOrder engPackingOrder);

    int submit(EngPackingOrder engPackingOrder);

    List<EngPackingOrderSummaryDetDto> checkQty(List<EngPackingOrderDto> engPackingOrderDtos);

    int register(EngPackingOrder engPackingOrder);

    int saveRecord(EngPackingOrder engPackingOrder, Byte logisticsNode, String title);
}
