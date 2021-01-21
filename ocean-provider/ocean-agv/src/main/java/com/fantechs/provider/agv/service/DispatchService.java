package com.fantechs.provider.agv.service;

import com.fantechs.common.base.support.IService;

import java.util.Map;

public interface DispatchService extends IService {

    String genAgvSchedulingTask(Map<String, Object> map);

    String continueTask(Map<String, Object> map);
}
