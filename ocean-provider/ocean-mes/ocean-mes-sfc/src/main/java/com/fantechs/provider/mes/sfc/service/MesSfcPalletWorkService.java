package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkScanDto;
import com.fantechs.common.base.general.dto.mes.sfc.RequestPalletWorkScanDto;

import java.util.List;

public interface MesSfcPalletWorkService {

    String palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception;

    List<PalletWorkScanDto> palletWorkScan();

    List<String> findPalletCarton(String palletCode);

    int submitNoFullPallet(List<Long> palletIdList) throws Exception;

    Boolean updatePalletType(Long stationId);
}
