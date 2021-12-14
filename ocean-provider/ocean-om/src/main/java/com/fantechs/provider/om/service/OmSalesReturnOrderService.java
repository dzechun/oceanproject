package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrder;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/06/21.
 */

public interface OmSalesReturnOrderService extends IService<OmSalesReturnOrder> {
    List<OmSalesReturnOrderDto> findList(Map<String, Object> map);

    List<OmHtSalesReturnOrderDto> findHtList(Map<String,Object> map);

//    int packageAutoOutOrder(OmSalesReturnOrder omSalesReturnOrder);

    int writeQty(OmSalesReturnOrderDet omSalesReturnOrderDet);

    int pushDown(String ids);
}
