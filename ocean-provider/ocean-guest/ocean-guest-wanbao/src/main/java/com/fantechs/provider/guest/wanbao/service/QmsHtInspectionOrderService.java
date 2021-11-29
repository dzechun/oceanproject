package com.fantechs.provider.guest.wanbao.service;

import com.fantechs.common.base.general.entity.wanbao.history.QmsHtInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */

public interface QmsHtInspectionOrderService extends IService<QmsHtInspectionOrder> {
    List<QmsHtInspectionOrder> findHtList(Map<String, Object> map);
}
