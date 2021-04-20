package com.fantechs.provider.agv.service;

import java.util.Map;

public interface DispatchService {

    String genAgvSchedulingTask(Map<String, Object> map);

    String continueTask(Map<String, Object> map);
}
