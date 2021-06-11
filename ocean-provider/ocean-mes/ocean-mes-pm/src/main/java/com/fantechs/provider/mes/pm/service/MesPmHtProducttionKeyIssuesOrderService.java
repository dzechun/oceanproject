package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProducttionKeyIssuesOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */

public interface MesPmHtProducttionKeyIssuesOrderService extends IService<MesPmHtProducttionKeyIssuesOrder> {
    List<MesPmHtProducttionKeyIssuesOrder> findHtList(Map<String, Object> map);
}
