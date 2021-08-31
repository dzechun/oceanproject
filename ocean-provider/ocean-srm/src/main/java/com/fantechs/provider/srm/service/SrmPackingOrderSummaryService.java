package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SrmPackingOrderSummaryService extends IService<SrmPackingOrderSummary> {

    List<SrmPackingOrderSummaryDto> findList(Map<String, Object> map);

    int batchAdd(List<SrmPackingOrderSummaryDto> srmPackingOrderSummaryDto);

    int save(SrmPackingOrderSummaryDto srmPackingOrderSummaryDto);

    Map<String, Object> importExcel(List<SrmPackingOrderSummaryImport> srmPackingOrderSummaryImports,Long packingOrderId);
}
