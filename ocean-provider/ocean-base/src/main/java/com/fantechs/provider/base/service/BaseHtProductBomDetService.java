package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseHtProductBomDetService extends IService<BaseHtProductBomDet> {

    List<BaseHtProductBomDet> findList(SearchBaseProductBomDet searchBaseProductBomDet);
}
