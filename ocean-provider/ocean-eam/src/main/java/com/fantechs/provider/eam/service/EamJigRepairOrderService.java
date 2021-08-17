package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */

public interface EamJigRepairOrderService extends IService<EamJigRepairOrder> {
    List<EamJigRepairOrderDto> findList(Map<String, Object> map);

    EamJigRepairOrderDto pdaCreateOrder(String jigBarcode);
}
