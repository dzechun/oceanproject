package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkScanDto;
import com.fantechs.common.base.general.dto.mes.sfc.RequestPalletWorkScanDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingListDto;

import java.util.List;

public interface MesSfcPalletWorkService {

    PalletWorkScanDto palletWorkScanBarcode(RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception;

    /**
     * 万宝-栈板作业（人工）
     * @param dto
     * @return
     * @throws Exception
     */
    int workByManualOperation(PalletWorkByManualOperationDto dto) throws Exception;

    /**
     * 万宝-栈板扫码（人工）
     * @param barcode
     * @return
     */
    ScanByManualOperationDto scanByManualOperation(String barcode, Long proLineId);

    List<PalletWorkScanDto> palletWorkScan(Long stationId);

    List<String> findPalletCarton(Long productPalletId);

    int submitNoFullPallet(List<Long> palletIdList, byte printBarcode, String printName, Long processId) throws Exception;

    Boolean updatePalletType(Long stationId);

    int updateNowPackageSpecQty(Long productPalletId, Double nowPackageSpecQty , Boolean print, String printName, Long processId) throws Exception;

    /**
     * 修改栈板状态为已转移
     * @param productPalletId
     * @return
     */
    int updateMoveStatus(Long productPalletId);


    Boolean testStacking(String code);

    /**
     * 堆码作业发送MQ
     * @param stackCode
     */
    void sendMQByStacking(String stackCode);

    /**
     * 万宝-堆垛作业（A线）
     * @param dto
     * @return
     * @throws Exception
     */
    int workByAuto(WanbaoAutoStackingListDto dto) throws Exception;

    /**
     * 校验条码同PO/销售明细/物料
     * @param barcodeList
     * @return
     */
    boolean checkBarCode(List<String> barcodeList);
}
