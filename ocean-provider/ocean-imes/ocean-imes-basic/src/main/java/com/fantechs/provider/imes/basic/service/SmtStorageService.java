package com.fantechs.provider.imes.basic.service;


import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/23.
 */

public interface SmtStorageService extends IService<SmtStorage> {

    int insert(SmtStorage storage);

    int batchDel(String ids);

    int updateById(SmtStorage storage);

    SmtStorage selectByKey(Long id);

    List<SmtStorage> findList(SearchSmtStorage searchSmtStorage);
}
