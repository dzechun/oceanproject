package com.fantechs.service.impl;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.mapper.DeliveryOrderUreportMapper;
import com.fantechs.service.DeliveryOrderUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryOrderUreportServiceImpl extends BaseService<WmsOutDeliveryOrderDet> implements DeliveryOrderUreportService {

    @Resource
    private DeliveryOrderUreportMapper deliveryOrderUreportMapper;

    @Override
    public List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map) {
        return deliveryOrderUreportMapper.findList(map);
    }
}
