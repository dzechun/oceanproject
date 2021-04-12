package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface MesSfcBarcodeProcessService extends IService<MesSfcBarcodeProcess> {
    List<MesSfcBarcodeProcessDto> findList(Map<String, Object> map);
}
