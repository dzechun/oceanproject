package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.OltSafeStockDto;
import com.fantechs.common.base.general.entity.basic.OltSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchOltSafeStock;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by mr.lei on 2021/03/04.
 */

public interface OltSafeStockService extends IService<OltSafeStock> {
    List<OltSafeStockDto> findList(SearchOltSafeStock searchOltSafeStock);
    List<OltSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock);
    int  inventeryWarning();
}
