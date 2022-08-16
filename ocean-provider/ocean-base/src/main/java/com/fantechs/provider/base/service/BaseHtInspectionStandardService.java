package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionStandard;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */

public interface BaseHtInspectionStandardService extends IService<BaseHtInspectionStandard> {
    List<BaseHtInspectionStandard> findHtList(Map<String, Object> map);
}
