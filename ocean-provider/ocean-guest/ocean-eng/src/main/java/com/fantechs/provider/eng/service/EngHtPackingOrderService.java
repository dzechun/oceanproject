package com.fantechs.provider.eng.service;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface EngHtPackingOrderService extends IService<EngHtPackingOrder> {
    List<EngHtPackingOrderDto> findList(Map<String, Object> map);
}
