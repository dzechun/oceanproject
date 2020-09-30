package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtProductProcessRouteMapper extends MyMapper<SmtProductProcessRoute> {

    List<SmtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute);
}