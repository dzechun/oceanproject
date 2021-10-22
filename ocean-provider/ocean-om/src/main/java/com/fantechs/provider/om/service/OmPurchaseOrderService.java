package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */

public interface OmPurchaseOrderService extends IService<OmPurchaseOrder> {
    List<OmPurchaseOrderDto> findList(Map<String, Object> map);

    OmPurchaseOrder saveByApi (OmPurchaseOrder omPurchaseOrder);

    int save(OmPurchaseOrder omPurchaseOrder);

    String findPurchaseMaterial(String purchaseOrderCode);
}
