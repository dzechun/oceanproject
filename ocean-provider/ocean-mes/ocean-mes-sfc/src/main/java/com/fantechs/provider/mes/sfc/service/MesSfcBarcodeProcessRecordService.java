package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface MesSfcBarcodeProcessRecordService extends IService<MesSfcBarcodeProcessRecord> {
    List<MesSfcBarcodeProcessRecordDto> findList(Map<String, Object> map);
}
