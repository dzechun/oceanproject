package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtPackingUnit;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2020/11/03.
 */

public interface BaseHtPackingUnitService extends IService<BaseHtPackingUnit> {

    List<BaseHtPackingUnit> findHtList(Map<String, Object> map);
}
