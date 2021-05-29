package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInventoryVerificationDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInventoryVerificationDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */

public interface WmsInnerStockOrderDetService extends IService<WmsInventoryVerificationDet> {
    List<WmsInventoryVerificationDetDto> findList(Map<String, Object> map);
}
