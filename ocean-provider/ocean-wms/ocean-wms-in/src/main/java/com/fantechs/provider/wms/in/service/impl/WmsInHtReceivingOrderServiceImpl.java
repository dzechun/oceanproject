package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtReceivingOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.mapper.WmsInHtReceivingOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInHtReceivingOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/15.
 */
@Service
public class WmsInHtReceivingOrderServiceImpl extends BaseService<WmsInHtReceivingOrder> implements WmsInHtReceivingOrderService {

    @Resource
    private WmsInHtReceivingOrderMapper wmsInHtReceivingOrderMapper;

    @Override
    public List<WmsInHtReceivingOrderDto> findList(Map<String, Object> map) {
        return wmsInHtReceivingOrderMapper.findHtList(map);
    }
}
