package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BaseHtLabelService extends IService<BaseHtLabel> {
    List<BaseHtLabel> findList(SearchBaseLabel searchBaseLabel);
}
