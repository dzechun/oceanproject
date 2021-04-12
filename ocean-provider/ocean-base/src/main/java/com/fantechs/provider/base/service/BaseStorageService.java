package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
/**
 *
 * Created by wcz on 2020/09/23.
 */

public interface BaseStorageService extends IService<BaseStorage> {

    List<BaseStorage> findList(SearchBaseStorage searchBaseStorage);

    //根据编码进行批量更新
    int batchUpdate(List<BaseStorage> baseStorages);
}
