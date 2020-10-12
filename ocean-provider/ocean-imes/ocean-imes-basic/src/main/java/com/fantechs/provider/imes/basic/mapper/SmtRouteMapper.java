package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtRouteMapper extends MyMapper<SmtRoute> {
    List<SmtRoute> findList(SearchSmtRoute searchSmtRoute);
}