package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerHtJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtJobOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/01.
 */

public interface WmsInnerHtJobOrderService extends IService<WmsInnerHtJobOrder> {
    List<WmsInnerHtJobOrderDto> findList(Map<String, Object> map);
}
