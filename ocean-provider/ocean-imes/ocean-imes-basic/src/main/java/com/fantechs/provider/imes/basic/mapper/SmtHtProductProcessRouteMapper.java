package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtProductProcessRouteMapper extends MyMapper<SmtHtProductProcessRoute> {

    List<SmtHtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute);
}