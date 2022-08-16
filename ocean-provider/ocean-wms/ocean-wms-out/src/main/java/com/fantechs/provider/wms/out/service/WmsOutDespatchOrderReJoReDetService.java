package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoReDetDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJoReDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */

public interface WmsOutDespatchOrderReJoReDetService extends IService<WmsOutDespatchOrderReJoReDet> {
    List<WmsOutDespatchOrderReJoReDetDto> findList(SearchWmsOutDespatchOrderReJoReDet searchWmsOutDespatchOrderReJoReDet);
}
