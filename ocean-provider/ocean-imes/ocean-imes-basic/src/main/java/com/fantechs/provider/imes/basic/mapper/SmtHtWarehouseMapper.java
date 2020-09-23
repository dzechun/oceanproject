package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtHtWarehouseMapper extends MyMapper<SmtHtWarehouse> {
    List<SmtHtWarehouse> findHtList(SearchSmtWarehouse searchSmtWarehouse);
}
