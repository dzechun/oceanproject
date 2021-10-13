package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface EngPackingOrderSummaryDetService extends IService<EngPackingOrderSummaryDet> {
    List<EngPackingOrderSummaryDetDto> findList(Map<String, Object> map);

    int batchAdd(List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos);

    int save(EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto);

    Map<String, Object> importExcel(List<EngPackingOrderSummaryDetImport> engPackingOrderSummaryDetImports, Long packingOrderSummaryId);

    List<EngPackingOrderSummaryDetDto> findListByIds(String ids);

    int addByContractQtyOrder(List<EngContractQtyOrderAndPurOrderDto> engContractQtyOrderAndPurOrderDtos,Long packingOrderSummaryId);

}
