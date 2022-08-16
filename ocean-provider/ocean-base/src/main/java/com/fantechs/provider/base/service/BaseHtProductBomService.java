package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseHtProductBomService extends IService<BaseHtProductBom> {

    List<BaseHtProductBom> findList(Map<String, Object> map);
}
