package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;

import java.util.List;

public interface SmtWarehouseService {
    int insert(SmtWarehouse smtWarehouse);

    int batchDel(String ids);

    int updateById(SmtWarehouse smtWarehouse);

    SmtWarehouse selectByKey(Long id);

    List<SmtWarehouse> findList(SearchSmtWarehouse searchSmtWarehouse);
}
