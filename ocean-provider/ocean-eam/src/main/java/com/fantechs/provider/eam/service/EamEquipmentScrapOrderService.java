package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentScrapOrderService extends IService<EamEquipmentScrapOrder> {
    List<EamEquipmentScrapOrderDto> findList(Map<String, Object> map);

    int save(EamEquipmentScrapOrderDto record);

    int update(EamEquipmentScrapOrderDto entity);

    int autoCreateOrder();
}
