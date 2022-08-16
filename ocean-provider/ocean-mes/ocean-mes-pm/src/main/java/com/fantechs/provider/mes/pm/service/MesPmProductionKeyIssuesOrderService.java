package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/11.
 */

public interface MesPmProductionKeyIssuesOrderService extends IService<MesPmProductionKeyIssuesOrder> {
    List<MesPmProductionKeyIssuesOrder> findList(Map<String, Object> map);
    MesPmProductionKeyIssuesOrder PDAFindOne(String workOrderCode);
}
