package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSafeStock;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by mr.lei on 2021/03/04.
 */

public interface BaseSafeStockService extends IService<BaseSafeStock> {
    List<BaseSafeStockDto> findList(SearchBaseSafeStock searchBaseSafeStock);
    List<BaseSafeStockDto> findHtList(SearchBaseSafeStock searchBaseSafeStock);
    int  inventeryWarning();
}
