package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.ScanBarcodeDto;

public interface ScanBarcodeService {

    int doScan(ScanBarcodeDto scanBarcodeDto) throws Exception;
}
