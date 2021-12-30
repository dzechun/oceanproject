package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaReBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaReBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvWarehouseAreaReBarcodeService extends IService<CallAgvWarehouseAreaReBarcode> {
    List<CallAgvWarehouseAreaReBarcodeDto> findList(Map<String, Object> map);
}
