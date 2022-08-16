package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */

public interface OmHtSalesOrderService extends IService<OmHtSalesOrder> {
    List<OmHtSalesOrderDto> findList(Map<String, Object> map);
}
