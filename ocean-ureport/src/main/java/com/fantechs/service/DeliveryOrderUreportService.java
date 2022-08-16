package com.fantechs.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;

import java.util.List;
import java.util.Map;

public interface DeliveryOrderUreportService {
    List<WmsOutDeliveryOrderDto> findList(Map<String, Object> dynamicConditionByEntity);
}
