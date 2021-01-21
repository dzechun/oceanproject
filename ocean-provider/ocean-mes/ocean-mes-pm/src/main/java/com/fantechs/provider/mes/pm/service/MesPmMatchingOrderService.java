package com.fantechs.provider.mes.pm.service;


import com.fantechs.common.base.general.dto.mes.pm.MesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMatchingOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/19.
 */

public interface MesPmMatchingOrderService extends IService<MesPmMatchingOrder> {
    List<MesPmMatchingOrderDto> findList(Map<String, Object> map);

    MesPmMatchingOrderDto findMinMatchingQuantity(String work_order_card_id);

    int save(SaveMesPmMatchingOrderDto saveMesPmMatchingOrderDto);
}
