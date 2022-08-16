package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/21.
 */

public interface BaseHtWorkShiftService extends IService<BaseHtWorkShift> {

    List<BaseHtWorkShift> findHtList(Map<String, Object> map);
}
