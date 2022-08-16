package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtConsignee;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/23.
 */

public interface BaseHtConsigneeService extends IService<BaseHtConsignee> {
    List<BaseHtConsignee> findHtList(Map<String, Object> map);
}
