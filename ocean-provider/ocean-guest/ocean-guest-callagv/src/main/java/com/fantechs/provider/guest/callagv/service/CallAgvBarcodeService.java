package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvBarcodeService extends IService<CallAgvBarcode> {
    List<CallAgvBarcodeDto> findList(Map<String, Object> map);
}
