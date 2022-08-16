package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseInspectionWay;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/19.
 */

public interface BaseInspectionWayService extends IService<BaseInspectionWay> {
    List<BaseInspectionWay> findList(Map<String, Object> map);
}
