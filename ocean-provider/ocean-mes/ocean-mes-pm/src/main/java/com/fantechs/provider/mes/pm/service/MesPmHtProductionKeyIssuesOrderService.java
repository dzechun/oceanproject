package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProductionKeyIssuesOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */

public interface MesPmHtProductionKeyIssuesOrderService extends IService<MesPmHtProductionKeyIssuesOrder> {
    List<MesPmHtProductionKeyIssuesOrder> findHtList(Map<String, Object> map);
}
