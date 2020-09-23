package com.fantechs.provider.imes.basic.service;

import com.fantechs.common.base.entity.basic.SmtHtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/23.
 */

public interface SmtHtStorageService extends IService<SmtHtStorage> {

    List<SmtHtStorage> findHtList(SearchSmtStorage searchSmtStorage);
}
