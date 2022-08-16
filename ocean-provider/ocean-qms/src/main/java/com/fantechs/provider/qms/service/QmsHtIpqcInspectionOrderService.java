package com.fantechs.provider.qms.service;


import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */

public interface QmsHtIpqcInspectionOrderService extends IService<QmsHtIpqcInspectionOrder> {
    List<QmsHtIpqcInspectionOrder> findHtList(Map<String, Object> map);
}
