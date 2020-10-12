package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtRouteProcessMapper extends MyMapper<SmtRouteProcess> {

    List<SmtRouteProcess> findList(Long routeId);
}