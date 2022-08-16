package com.fantechs.security.service;


import com.fantechs.common.base.entity.security.history.SysHtSpecItem;

import java.util.List;
import java.util.Map;

public interface SysHtSpecItemService {
    List<SysHtSpecItem> findHtSpecItemList(Map<String, Object> map);
}
