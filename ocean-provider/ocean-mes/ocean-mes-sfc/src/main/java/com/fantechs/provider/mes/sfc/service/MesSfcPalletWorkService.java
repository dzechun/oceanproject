package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkScanDto;
import com.fantechs.common.base.general.dto.mes.sfc.RequestPalletWorkScanDto;

import java.util.List;

public interface MesSfcPalletWorkService {

    PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception;

    List<PalletWorkScanDto> palletWorkScan(Long stationId);

    List<String> findPalletCarton(Long productPalletId);

    int submitNoFullPallet(List<Long> palletIdList) throws Exception;

    Boolean updatePalletType(Long stationId);
}
