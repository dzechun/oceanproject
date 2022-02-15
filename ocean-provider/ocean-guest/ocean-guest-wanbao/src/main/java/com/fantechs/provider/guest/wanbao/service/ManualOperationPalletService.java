package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;

import java.util.List;

public interface ManualOperationPalletService {

    /**
     * 万宝-栈板作业（人工）
     * @param dto
     * @return
     * @throws Exception
     */
    int workByManualOperation(PalletWorkByManualOperationDto dto);

    /**
     * 万宝-栈板扫码（人工）
     * @param barcode
     * @param proLineId
     * @return
     */
    ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId);

    /**
     * 万宝-栈板扫堆垛码（人工）
     * @param stackingCode
     * @param proLineId
     * @return
     */
    List<WanbaoStackingDto> scanStackingCode(String stackingCode, Long proLineId);
}
