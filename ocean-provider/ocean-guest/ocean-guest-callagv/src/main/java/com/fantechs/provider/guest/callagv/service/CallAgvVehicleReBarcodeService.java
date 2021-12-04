package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.*;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvStorageMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvVehicleReBarcodeService extends IService<CallAgvVehicleReBarcode> {
    List<CallAgvVehicleReBarcodeDto> findList(Map<String, Object> map);

    List<CallAgvVehicleReBarcode> callAgvStock(RequestCallAgvStockDTO requestCallAgvStockDTO) throws BizErrorException;

    String callAgvDistribution(Long vehicleId, Long warehouseAreaId, Long storageTaskPointId, Integer type) throws Exception;

    String callAgvDistributionRest(CallAgvDistributionRestDto callAgvDistributionRestDto) throws Exception;

    String genAgvSchedulingTask(String taskTyp, List<String> positionCodeList, String podCode) throws Exception;

    int vehicleBarcodeUnbound(RequestBarcodeUnboundDTO requestBarcodeUnboundDTO);

    List<CallAgvVehicleBarcodeDTO> findCallAgvVehicleList(Map<String, Object> map);

    int vehicleDisplacement(Long vehicleId, Long storageTaskPointId, Integer type) throws Exception;

    int materialTransfer(RequestCallAgvStockDTO requestCallAgvStockDTO);

    List<CallAgvWarehouseAreaMaterialDto> agvWarehouseAreaMaterialSummary(SearchCallAgvStorageMaterial SearchCallAgvStorageMaterial);

    List<CallAgvStorageMaterialDto> agvStorageMaterialDetail(SearchCallAgvStorageMaterial SearchCallAgvStorageMaterial);
}
