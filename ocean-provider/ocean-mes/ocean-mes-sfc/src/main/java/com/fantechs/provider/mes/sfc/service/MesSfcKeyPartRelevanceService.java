package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/05/08.
 */

public interface MesSfcKeyPartRelevanceService extends IService<MesSfcKeyPartRelevance> {
    List<MesSfcKeyPartRelevanceDto> findList(Map<String, Object> map);

    List<MesSfcKeyPartRelevanceDto> findListByPallet(Map<String, Object> map);

    List<MesSfcKeyPartRelevanceDto> findListForGroup(Map<String, Object> map);

    /**
     * 条码解绑
     * @param barcode
     */
    boolean barcodeUnbinding(String barcode);
}
