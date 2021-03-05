package com.fantechs.provider.bcm.service;

import com.fantechs.common.base.general.dto.bcm.OltSafeStockDto;
import com.fantechs.common.base.general.entity.bcm.OltSafeStock;
import com.fantechs.common.base.general.entity.bcm.search.SearchOltSafeStock;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/03/04.
 */

public interface OltSafeStockService extends IService<OltSafeStock> {
    List<OltSafeStockDto> findList(SearchOltSafeStock searchOltSafeStock);
    List<OltSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock);
    int  inventeryWarning();
}
