package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummaryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SrmPackingOrderSummaryDetService extends IService<SrmPackingOrderSummaryDet> {
    List<SrmPackingOrderSummaryDetDto> findList(Map<String, Object> map);

    int batchAdd(List<SrmPackingOrderSummaryDetDto> srmPackingOrderSummaryDetDtos);

    int save(SrmPackingOrderSummaryDetDto srmPackingOrderSummaryDetDto);

    Map<String, Object> importExcel(List<SrmPackingOrderSummaryDetImport> srmPackingOrderSummaryDetImports);
}
