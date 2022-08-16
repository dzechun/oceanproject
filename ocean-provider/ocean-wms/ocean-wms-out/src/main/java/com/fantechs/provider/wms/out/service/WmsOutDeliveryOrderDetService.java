package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */

public interface WmsOutDeliveryOrderDetService extends IService<WmsOutDeliveryOrderDet> {
    List<WmsOutDeliveryOrderDetDto> findList(Map<String, Object> map);
}
