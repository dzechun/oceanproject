package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.out.mapper.WmsOutDeliveryOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsOutDeliveryOrderServiceImpl extends BaseService<WmsOutDeliveryOrder> implements WmsOutDeliveryOrderService {

    @Resource
    private WmsOutDeliveryOrderMapper wmsOutDeliveryOrderMapper;

    @Override
    public List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map) {
        return wmsOutDeliveryOrderMapper.findList(map);
    }
}
