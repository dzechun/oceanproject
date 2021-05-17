package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.PdaCartonAnnexDto;
import com.fantechs.common.base.general.dto.mes.sfc.PdaCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.PdaCartonRecordDto;
import com.fantechs.common.base.general.dto.mes.sfc.PdaPutIntoProductionDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;

import java.math.BigDecimal;
import java.util.List;

public interface MesSfcBarcodeOperationService {

    /**
     * 投产作业
     * @param vo
     * @return
     */
    int pdaPutIntoProduction(PdaPutIntoProductionDto vo) throws Exception;


    PdaCartonRecordDto findLastCarton(Long processId, Long stationId, String packType);

    /**
     * 包箱作业-扫条码
     * @param vo
     * @return
     */
    int cartonOperation(PdaCartonDto vo) throws Exception;

    /**
     * 包箱作业-扫附件码
     * @param vo
     * @return
     */
    int cartonAnnexOperation(PdaCartonAnnexDto vo) throws Exception;

    /**
     * 包箱作业-修改包箱规格数量
     * @param productCartonId
     * @param cartonDescNum
     * @return
     */
    int updateCartonDescNum(Long productCartonId, BigDecimal cartonDescNum, String packType);

    /**
     * 包箱作业-检查是否有未关闭包箱
     * @param stationId
     * @return
     */
    List<MesSfcProductCarton> findCartonByStationId(Long stationId);
}
