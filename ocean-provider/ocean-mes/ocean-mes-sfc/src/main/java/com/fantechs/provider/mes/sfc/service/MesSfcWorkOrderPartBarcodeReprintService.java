package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderPartBarcodeReprintDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderPartBarcodeReprint;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/06/17.
 */

public interface MesSfcWorkOrderPartBarcodeReprintService extends IService<MesSfcWorkOrderPartBarcodeReprint> {
    List<MesSfcWorkOrderPartBarcodeReprintDto> findList(Map<String, Object> map);
}
