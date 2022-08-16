package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtWarning;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/03.
 */

public interface BaseHtWarningService extends IService<BaseHtWarning> {
    List<BaseHtWarning> findHtList(Map<String, Object> map);
}
