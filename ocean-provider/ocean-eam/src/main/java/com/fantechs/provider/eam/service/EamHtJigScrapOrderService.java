package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigScrapOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/19.
 */

public interface EamHtJigScrapOrderService extends IService<EamHtJigScrapOrder> {
    List<EamHtJigScrapOrder> findHtList(Map<String, Object> map);
}
