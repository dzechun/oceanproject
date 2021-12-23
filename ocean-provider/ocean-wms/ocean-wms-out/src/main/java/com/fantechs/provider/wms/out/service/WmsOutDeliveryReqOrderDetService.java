package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */

public interface WmsOutDeliveryReqOrderDetService extends IService<WmsOutDeliveryReqOrderDet> {
    List<WmsOutDeliveryReqOrderDetDto> findList(Map<String, Object> map);
    List<WmsOutHtDeliveryReqOrderDet> findHtList(Map<String, Object> map);
    int updateActualQty(WmsOutDeliveryReqOrderDetDto wmsOutDeliveryReqOrderDetDto);
}
