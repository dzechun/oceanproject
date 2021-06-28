package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;

import java.util.List;

/**
 * 仓库作业-库内移位作业
 */
public interface WmsInnerShiftWorkService {

    List<WmsInnerJobOrderDto> pdaFindList(String jobOrderCode);

    List<WmsInnerJobOrderDetDto> pdaFindDetList(Long jobOrderId);
}
