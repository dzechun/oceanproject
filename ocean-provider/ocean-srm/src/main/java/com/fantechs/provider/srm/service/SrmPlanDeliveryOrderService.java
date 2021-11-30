package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPlanDeliveryOrderImport;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface SrmPlanDeliveryOrderService extends IService<SrmPlanDeliveryOrder> {
    List<SrmPlanDeliveryOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<SrmPlanDeliveryOrderImport> list);
}
