package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReprintDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReprint;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/16.
 */

public interface WmsInnerMaterialBarcodeReprintService extends IService<WmsInnerMaterialBarcodeReprint> {
    List<WmsInnerMaterialBarcodeReprintDto> findList(Map<String, Object> map);

}
