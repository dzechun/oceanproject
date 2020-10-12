package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtRouteMapper extends MyMapper<SmtHtRoute> {
    List<SmtRoute> findList(SearchSmtRoute searchSmtRoute);
}