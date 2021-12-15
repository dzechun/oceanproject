package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtReceivingOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/15.
 */

public interface WmsInHtReceivingOrderDetService extends IService<WmsInHtReceivingOrderDet> {
    List<WmsInHtReceivingOrderDetDto> findList(Map<String, Object> map);
}
