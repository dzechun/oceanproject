package com.fantechs.auth.mapper;

import com.fantechs.common.base.entity.security.history.SysHtSpecItem;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SysHtSpecItemMapper extends MyMapper<SysHtSpecItem> {

    List<SysHtSpecItem> findHtSpecItemList(Map<String, Object> map);
}