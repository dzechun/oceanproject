package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProducttionKeyIssuesOrder;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */

public interface MesPmProducttionKeyIssuesOrderService extends IService<MesPmProducttionKeyIssuesOrder> {
    List<MesPmProducttionKeyIssuesOrder> findList(Map<String, Object> map);
    MesPmProducttionKeyIssuesOrder PDAFindOne(String workOrderCode);
}
