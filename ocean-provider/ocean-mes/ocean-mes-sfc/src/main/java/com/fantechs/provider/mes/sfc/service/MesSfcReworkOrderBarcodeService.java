package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrderBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/15.
 */

public interface MesSfcReworkOrderBarcodeService extends IService<MesSfcReworkOrderBarcode> {
    List<MesSfcReworkOrderBarcodeDto> findList(Map<String, Object> map);
    List<MesSfcHtReworkOrderBarcodeDto> findHtList(Map<String, Object> map);
}
