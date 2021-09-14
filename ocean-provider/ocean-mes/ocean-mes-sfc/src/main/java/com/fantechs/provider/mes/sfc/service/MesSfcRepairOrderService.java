package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrder;
import com.fantechs.common.base.general.entity.mes.sfc.history.MesSfcHtRepairOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/10.
 */

public interface MesSfcRepairOrderService extends IService<MesSfcRepairOrder> {
    List<MesSfcRepairOrderDto> findList(Map<String, Object> map);

    List<MesSfcHtRepairOrder> findHtList(Map<String, Object> map);

    MesPmWorkOrderDto getWorkOrder(String SNCode, String workOrderCode);
}
