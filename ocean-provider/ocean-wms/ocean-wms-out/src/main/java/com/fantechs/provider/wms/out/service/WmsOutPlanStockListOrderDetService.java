package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;

import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/22.
 */

public interface WmsOutPlanStockListOrderDetService extends IService<WmsOutPlanStockListOrderDet> {
    List<WmsOutPlanStockListOrderDetDto> findList(SearchWmsOutPlanStockListOrderDet searchWmsOutPlanStockListOrderDet);

    //Map<String, Object> importExcel(List<WmsOutPlanStockListOrderDet> list);
}
