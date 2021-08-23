package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */

public interface EamJigPointInspectionOrderService extends IService<EamJigPointInspectionOrder> {
    List<EamJigPointInspectionOrderDto> findList(Map<String, Object> map);

    EamJigPointInspectionOrderDto pdaCreateOrder(String jigBarcode);

    int pdaSubmit(EamJigPointInspectionOrder eamJigPointInspectionOrder);
}
