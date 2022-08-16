package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/17.
 */

public interface MesSfcWorkOrderPartBarcodeService extends IService<MesSfcWorkOrderPartBarcode> {
    List<MesSfcWorkOrderPartBarcodeDto> findList(Map<String, Object> map);
}
