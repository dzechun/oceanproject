package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/13.
 */

public interface WmsInReceivingOrderDetService extends IService<WmsInReceivingOrderDet> {
    List<WmsInReceivingOrderDetDto> findList(Map<String, Object> map);
}
