package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInFinishedProduct;
import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtFinishedProduct;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/01/07.
 */

public interface WmsInFinishedProductService extends IService<WmsInFinishedProduct> {

    List<WmsInFinishedProductDto> findList(Map<String, Object> dynamicConditionByEntity);

    List<WmsInHtFinishedProduct> findHtList(Map<String, Object> dynamicConditionByEntity);

    int PDASubmit(WmsInFinishedProduct wmsInFinishedProduct);
}
