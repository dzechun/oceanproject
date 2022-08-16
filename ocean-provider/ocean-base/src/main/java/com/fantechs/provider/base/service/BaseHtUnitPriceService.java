package com.fantechs.provider.base.service;


import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/27.
 */

public interface BaseHtUnitPriceService extends IService<BaseHtUnitPrice> {
    List<BaseHtUnitPrice> findHtList(Map<String, Object> map);
}
