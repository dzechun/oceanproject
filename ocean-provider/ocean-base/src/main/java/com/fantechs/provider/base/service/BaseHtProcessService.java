package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/25.
 */

public interface BaseHtProcessService extends IService<BaseHtProcess> {

    List<BaseHtProcess> findHtList(SearchBaseProcess searchBaseProcess);
}
