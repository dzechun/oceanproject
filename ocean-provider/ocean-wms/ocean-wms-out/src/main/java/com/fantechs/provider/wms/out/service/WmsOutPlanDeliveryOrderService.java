package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutPlanDeliveryOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */

public interface WmsOutPlanDeliveryOrderService extends IService<WmsOutPlanDeliveryOrder> {
    List<WmsOutPlanDeliveryOrderDto> findList(Map<String, Object> map);
    List<WmsOutHtPlanDeliveryOrder> findHtList(Map<String, Object> map);
    Map<String, Object> importExcel(List<WmsOutPlanDeliveryOrderImport> wmsOutPlanDeliveryOrderImports);
    int save(WmsOutPlanDeliveryOrderDto record);
    int update(WmsOutPlanDeliveryOrderDto entity);
    int pushDown(List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos);
}
