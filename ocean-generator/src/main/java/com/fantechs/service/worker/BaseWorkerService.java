package com.fantechs.service.worker;

import com.fantechs.model.BaseWorker;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/04/23.
 */

public interface BaseWorkerService extends IService<BaseWorker> {
    List<BaseWorkerDto> findList(Map<String, Object> map);
}
