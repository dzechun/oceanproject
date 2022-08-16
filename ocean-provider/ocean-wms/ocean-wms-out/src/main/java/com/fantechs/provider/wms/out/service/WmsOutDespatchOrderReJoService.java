package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderReJoDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrderReJo;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrderReJo;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/10.
 */

public interface WmsOutDespatchOrderReJoService extends IService<WmsOutDespatchOrderReJo> {
    List<WmsOutDespatchOrderReJoDto> findList(SearchWmsOutDespatchOrderReJo searchWmsOutDespatchOrderReJo);
}
