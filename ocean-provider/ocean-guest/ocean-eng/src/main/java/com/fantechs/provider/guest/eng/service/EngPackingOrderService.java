package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
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
}
