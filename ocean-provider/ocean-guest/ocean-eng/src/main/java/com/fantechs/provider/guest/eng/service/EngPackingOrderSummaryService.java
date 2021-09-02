package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface EngPackingOrderSummaryService extends IService<EngPackingOrderSummary> {

    List<EngPackingOrderSummaryDto> findList(Map<String, Object> map);

    int batchAdd(List<EngPackingOrderSummaryDto> engPackingOrderSummaryDto);

    int save(EngPackingOrderSummaryDto engPackingOrderSummaryDto);

    Map<String, Object> importExcel(List<EngPackingOrderSummaryImport> engPackingOrderSummaryImports, Long packingOrderId);
}
