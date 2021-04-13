package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseRouteProcessMapper extends MyMapper<BaseRouteProcess> {

    List<BaseRouteProcess> findList(@Param(value="routeId")Long routeId);

    int configureProcess(Map<String, Object> map);
}