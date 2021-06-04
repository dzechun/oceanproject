package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;

import java.math.BigDecimal;
import java.util.List;

public interface MesSfcBarcodeOperationService {

    /**
     * 包箱作业
     * @param dto
     * @return
     */
    int pdaCartonWork(PdaCartonWorkDto dto) throws Exception;

    /**
     * 查找包箱数据
     * @param processId
     * @param stationId
     * @param packType
     * @return
     */
    PdaCartonRecordDto findLastCarton(Long processId, Long stationId, String packType);

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

    /**
     * 未满箱提交关箱
     * 必须条码跟附件码是满足工单清单的情况才允许关箱
     * @param dto
     * @return
     */
    int closeCarton(CloseCartonDto dto) throws Exception;
}
