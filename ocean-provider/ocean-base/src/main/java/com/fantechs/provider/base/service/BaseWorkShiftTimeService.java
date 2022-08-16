package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/04.
 */
public interface BaseWorkShiftTimeService extends IService<BaseWorkShiftTime> {

    List<BaseWorkShiftTime> findList(Map<String, Object> map);
}
