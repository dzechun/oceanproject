package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutTransferDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */

public interface WmsOutDeliveryOrderService extends IService<WmsOutDeliveryOrder> {
    List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map);

    List<WmsOutTransferDeliveryOrderDto> transferFindList(Map<String, Object> map);

    List<WmsOutTransferDeliveryOrderDto> transferFindHtList(Map<String, Object> map);

    List<WmsOutHtDeliveryOrder> findHtList(Map<String, Object> map);

    int createJobOrder(Long id);

    int forwardingStatus(Long deliverOrderId,Byte orderStatus);

    /**
     * 批量修改审核状态
     * @param ids
     * @return
     */
    int updateStatus(List<Long> ids);
}
