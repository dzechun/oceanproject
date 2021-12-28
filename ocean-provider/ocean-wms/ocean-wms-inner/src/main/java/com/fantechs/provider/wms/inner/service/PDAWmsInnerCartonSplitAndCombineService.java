package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;

/**
 * 拣货单
 * @Author mr.lei
 * @Date 2021/5/10
 */
public interface PDAWmsInnerCartonSplitAndCombineService {
    WmsInnerInventoryDetDto checkCartonCode(String cartonCode, Integer type);
}
