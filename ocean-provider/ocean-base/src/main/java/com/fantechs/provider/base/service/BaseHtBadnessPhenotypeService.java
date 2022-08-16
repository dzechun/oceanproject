package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessPhenotype;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/07.
 */

public interface BaseHtBadnessPhenotypeService extends IService<BaseHtBadnessPhenotype> {
    List<BaseHtBadnessPhenotype> findList(Map<String, Object> map);
}
