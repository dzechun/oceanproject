package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/23.
 */

public interface BaseHtStorageService extends IService<BaseHtStorage> {

    List<BaseHtStorage> findHtList(SearchBaseStorage searchBaseStorage);
}
