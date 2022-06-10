package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutPlanStockListOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanStockListOrder;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */

public interface WmsOutPlanStockListOrderService extends IService<WmsOutPlanStockListOrder> {
    List<WmsOutPlanStockListOrderDto> findList(Map<String, Object> map);

    List<WmsOutHtPlanStockListOrder> findHtList(Map<String, Object> map);

    int save(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto);

    int update(WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto);

    int pushDown(List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos);

    int updateActualQty(Long planStockListOrderDetId, BigDecimal actualQty);

    Map<String, Object> importExcel(List<WmsOutPlanStockListOrderImport> wmsOutPlanStockListOrderImports);



    //Map<String, Object> importExcel(List<WmsOutPlanStockListOrder> list);
}