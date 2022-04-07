package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.ScanBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.WanbaoStackingMQDto;

public interface ScanBarcodeService {

    WanbaoStackingMQDto doScan(ScanBarcodeDto scanBarcodeDto) throws Exception;
}
