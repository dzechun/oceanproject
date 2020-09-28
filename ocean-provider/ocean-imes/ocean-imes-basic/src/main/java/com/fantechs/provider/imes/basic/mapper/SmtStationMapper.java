package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtStation;
import com.fantechs.common.base.entity.basic.search.SearchSmtStation;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtStationMapper extends MyMapper<SmtStation> {
    List<SmtStation> findList(SearchSmtStation searchSmtStation);
}