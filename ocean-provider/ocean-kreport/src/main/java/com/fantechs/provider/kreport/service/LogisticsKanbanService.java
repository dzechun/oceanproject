package com.fantechs.provider.kreport.service;

import com.fantechs.common.base.general.entity.kreport.CarrierProcessingOrder;
import com.fantechs.common.base.general.entity.kreport.LogisticsKanban;
import com.fantechs.common.base.general.entity.kreport.TransportInformation;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface LogisticsKanbanService extends IService<LogisticsKanban> {
    LogisticsKanban findKanbanData(Map<String, Object> map);

    /**
     * 运输地信息
     */
    List<TransportInformation> findTransportInformationList(Map<String,Object> map);

    List<CarrierProcessingOrder> findCarrierProcessingOrderList(Map<String,Object> map);

}
