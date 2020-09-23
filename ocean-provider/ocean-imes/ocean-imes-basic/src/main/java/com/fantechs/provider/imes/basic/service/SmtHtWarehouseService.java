package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;

import java.util.List;

public interface SmtHtWarehouseService {
    List<SmtHtWarehouse> findHtList(SearchSmtWarehouse searchSmtWarehouse);
}
