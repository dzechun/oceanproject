package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/01.
 */

public interface WmsInnerInitStockDetService extends IService<WmsInnerInitStockDet> {
    List<WmsInnerInitStockDetDto> findList(Map<String, Object> map);

    WmsInnerInitStockDet commit(WmsInnerInitStockDet wmsInnerInitStockDet);
}
