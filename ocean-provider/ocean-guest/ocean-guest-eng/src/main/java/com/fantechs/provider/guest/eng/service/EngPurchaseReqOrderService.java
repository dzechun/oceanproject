package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngPurchaseReqOrderDto;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/09/02.
 */

public interface EngPurchaseReqOrderService extends IService<EngPurchaseReqOrder> {
    List<EngPurchaseReqOrderDto> findList(Map<String, Object> map);

    int saveByApi (EngPurchaseReqOrder engPurchaseReqOrder);
}
