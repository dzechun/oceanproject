package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryLog;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/07/29.
 */

public interface WmsInnerInventoryLogService extends IService<WmsInnerInventoryLog> {
    List<WmsInnerInventoryLogDto> findList(Map<String, Object> map);

    BigDecimal findInv(Map<String ,Object> map);

    String findInvName(Long inventoryStatusId);
}
