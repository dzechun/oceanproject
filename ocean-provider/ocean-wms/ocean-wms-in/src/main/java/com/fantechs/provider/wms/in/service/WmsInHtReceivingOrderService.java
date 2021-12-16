package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtReceivingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/15.
 */

public interface WmsInHtReceivingOrderService extends IService<WmsInHtReceivingOrder> {
    List<WmsInHtReceivingOrderDto> findList(Map<String, Object> map);

}
