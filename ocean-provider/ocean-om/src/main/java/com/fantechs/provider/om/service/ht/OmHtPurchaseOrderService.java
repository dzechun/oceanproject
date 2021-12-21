package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.dto.om.OmHtPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/20.
 */

public interface OmHtPurchaseOrderService extends IService<OmHtPurchaseOrder> {
    List<OmHtPurchaseOrderDto> findList(Map<String, Object> map);
}
