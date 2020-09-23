package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtWarehouseMapper extends MyMapper<SmtWarehouse> {
    List<SmtWarehouse> findList(SearchSmtWarehouse searchSmtWarehouse);
}