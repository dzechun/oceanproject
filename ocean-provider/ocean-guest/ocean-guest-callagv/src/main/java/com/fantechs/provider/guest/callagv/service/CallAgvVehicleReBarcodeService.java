package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleBarcodeDTO;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleReBarcodeDto;
import com.fantechs.common.base.general.dto.callagv.RequestBarcodeUnboundDTO;
import com.fantechs.common.base.general.dto.callagv.RequestCallAgvStockDTO;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/09/10.
 */

public interface CallAgvVehicleReBarcodeService extends IService<CallAgvVehicleReBarcode> {
    List<CallAgvVehicleReBarcodeDto> findList(Map<String, Object> map);

    int callAgvStock(RequestCallAgvStockDTO requestCallAgvStockDTO) throws Exception;

    String callAgvDistribution(Long vehicleId, Long warehouseAreaId, Integer type) throws Exception;

    String genAgvSchedulingTask(String taskTyp, List<String> positionCodeList) throws Exception;

    int vehicleBarcodeUnbound(RequestBarcodeUnboundDTO requestBarcodeUnboundDTO);

    List<CallAgvVehicleBarcodeDTO> findCallAgvVehicleList(Map<String, Object> map);

    int CallAgvVehicle(Long vehicleId, Long warehouseAreaId) throws Exception;
}
