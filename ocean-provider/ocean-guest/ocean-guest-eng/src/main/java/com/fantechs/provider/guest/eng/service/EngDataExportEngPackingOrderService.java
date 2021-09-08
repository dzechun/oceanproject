package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.restapi.EngDataExportEngPackingOrderDto;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */

public interface EngDataExportEngPackingOrderService extends IService<EngDataExportEngPackingOrderDto> {
    List<EngDataExportEngPackingOrderDto> findExportData(Map<String, Object> map);

    String writePackingLists ();
}
