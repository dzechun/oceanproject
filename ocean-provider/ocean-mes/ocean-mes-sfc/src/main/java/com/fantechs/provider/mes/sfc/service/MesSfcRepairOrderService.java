package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.basic.BaseProductBomDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderPrintParam;
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

    MesSfcRepairOrderDto getWorkOrder(String SNCode, String workOrderCode, Integer SNCodeType);

    int print(MesSfcRepairOrderPrintParam mesSfcRepairOrderPrintParam);

    MesSfcRepairOrder add(MesSfcRepairOrder record);

    List<BaseProductBomDetDto> findSemiProductBom(String semiProductBarcode);
}
