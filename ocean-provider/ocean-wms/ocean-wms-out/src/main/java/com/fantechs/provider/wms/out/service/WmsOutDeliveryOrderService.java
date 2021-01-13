package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2021/01/09.
 */

public interface WmsOutDeliveryOrderService extends IService<WmsOutDeliveryOrder> {

    List<WmsOutDeliveryOrderDto> findList(Map<String, Object> dynamicConditionByEntity);

    List<WmsOutDeliveryOrderDto> findHtList(Map<String, Object> dynamicConditionByEntity);
}