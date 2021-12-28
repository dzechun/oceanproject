package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaBarcodeLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaBarcodeLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvWarehouseAreaBarcodeLogService extends IService<CallAgvWarehouseAreaBarcodeLog> {
    List<CallAgvWarehouseAreaBarcodeLogDto> findList(Map<String, Object> map);
}
