package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.support.IService;

import java.util.List;

public interface SmtWarehouseService extends IService<SmtWarehouse>{
    int insert(SmtWarehouse smtWarehouse);

    int batchDel(String ids);

    int updateById(SmtWarehouse smtWarehouse);

    SmtWarehouse selectByKey(Long id);

    List<SmtWarehouse> findList(SearchSmtWarehouse searchSmtWarehouse);
}
