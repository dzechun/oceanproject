package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorker;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */

public interface BaseHtWorkerService extends IService<BaseHtWorker> {
    List<BaseHtWorker> findList(Map<String, Object> map);
}
