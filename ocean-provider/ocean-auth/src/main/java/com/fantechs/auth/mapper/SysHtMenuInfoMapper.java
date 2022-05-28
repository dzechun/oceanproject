package com.fantechs.auth.mapper;

import com.fantechs.common.base.entity.security.history.SysHtMenuInfo;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface SysHtMenuInfoMapper extends MyMapper<SysHtMenuInfo> {
    List<SysHtMenuInfo> findHtList(Map<String, Object> map);
}