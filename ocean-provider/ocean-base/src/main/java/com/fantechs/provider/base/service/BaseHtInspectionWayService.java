package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionWay;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */

public interface BaseHtInspectionWayService extends IService<BaseHtInspectionWay> {
    List<BaseHtInspectionWay> findHtList(Map<String, Object> map);
}
