package com.fantechs.provider.om.service.sales;


import com.fantechs.common.base.general.dto.om.sales.OmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.sales.OmSalesOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/19.
 */

public interface OmSalesOrderService extends IService<OmSalesOrder> {
    List<OmSalesOrderDto> findList(Map<String, Object> map);

    List<OmSalesOrderDto> findHtList(Map<String, Object> map);
}
