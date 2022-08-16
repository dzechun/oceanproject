package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/15.
 */

public interface BaseHtProductFamilyService extends IService<BaseHtProductFamily> {

    List<BaseHtProductFamily> findHtList(Map<String, Object> map);
}
