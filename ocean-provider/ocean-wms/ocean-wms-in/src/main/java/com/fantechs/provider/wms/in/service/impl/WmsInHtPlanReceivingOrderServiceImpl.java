package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtPlanReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtPlanReceivingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInHtPlanReceivingOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInHtPlanReceivingOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@Service
public class WmsInHtPlanReceivingOrderServiceImpl extends BaseService<WmsInHtPlanReceivingOrder> implements WmsInHtPlanReceivingOrderService {

    @Resource
    private WmsInHtPlanReceivingOrderMapper wmsInHtPlanReceivingOrderMapper;

    @Override
    public List<WmsInHtPlanReceivingOrderDto> findList(Map<String, Object> map) {
        return wmsInHtPlanReceivingOrderMapper.findHtList(map);
    }
}
