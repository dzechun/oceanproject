package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtStationMapper extends MyMapper<SmtHtStation> {
    List<SmtHtStation> findList(SearchSmtStation searchSmtStation);
}