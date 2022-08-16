package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/15.
 */

public interface BaseHtPlatePartsService extends IService<BaseHtPlateParts> {
    List<BaseHtPlateParts> findHtList(Map<String, Object> map);
}
