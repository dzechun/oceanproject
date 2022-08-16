package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleStandard;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/06.
 */

public interface BaseHtSampleStandardService extends IService<BaseHtSampleStandard> {
    List<BaseHtSampleStandard> findHtList(Map<String, Object> map);
}
