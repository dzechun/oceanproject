package com.fantechs.provider.wms.out.service;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;

import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrder;
import com.fantechs.common.base.support.IService;


import java.math.BigDecimal;
import java.util.List;
/**
 *
 * Created by leifengzhi on 2021/12/22.
 */

public interface WmsOutPlanStockListOrderService extends IService<WmsOutPlanStockListOrder> {
    List<WmsOutPlanStockListOrderDto> findList(SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder);

    int save(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto);

    int update(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto);

    int pushDown(List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos);

    int updateActualQty(Long planStockListOrderDetId, BigDecimal actualQty);


    //Map<String, Object> importExcel(List<WmsOutPlanStockListOrder> list);
}
