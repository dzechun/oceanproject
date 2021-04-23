package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
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
