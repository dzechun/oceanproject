package com.fantechs.provider.guest.callagv.service;

import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface CallAgvAgvTaskService extends IService<CallAgvAgvTask> {
    List<CallAgvAgvTaskDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<CallAgvAgvTask> list);
}
