package com.fantechs.security.service;

import com.fantechs.common.base.entity.security.history.SysHtMenuInfo;

import java.util.List;
import java.util.Map;

public interface SysHtMenuInfoService {
    List<SysHtMenuInfo> findHtList(Map<String, Object> map);
}
