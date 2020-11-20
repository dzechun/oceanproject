package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SmtRouteProcessMapper extends MyMapper<SmtRouteProcess> {

    List<SmtRouteProcess> findList(@Param(value="routeId")Long routeId);

    int configureProcess(Map<String, Object> map);
}