package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/09.
 */

public interface BaseHtBadnessCauseService extends IService<BaseHtBadnessCause> {
    List<BaseHtBadnessCause> findHtList(Map<String, Object> map);
}
