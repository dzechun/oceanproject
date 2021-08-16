package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/13.
 */

public interface EamJigMaintainOrderService extends IService<EamJigMaintainOrder> {
    List<EamJigMaintainOrderDto> findList(Map<String, Object> map);

    EamJigMaintainOrderDto pdaCreateOrder(String jigBarcode);
}
