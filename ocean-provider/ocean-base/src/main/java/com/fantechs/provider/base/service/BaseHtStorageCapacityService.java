package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageCapacity;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/18.
 */

public interface BaseHtStorageCapacityService extends IService<BaseHtStorageCapacity> {
    List<BaseHtStorageCapacity> findHtList(Map<String, Object> map);
}
