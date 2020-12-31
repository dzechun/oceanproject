package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutFinishedProductDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtFinishedProduct;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutFinishedProduct;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2020/12/22.
 */

public interface WmsOutFinishedProductService extends IService<WmsOutFinishedProduct> {

    List<WmsOutFinishedProductDto> findList(Map<String, Object> map);

    List<WmsOutHtFinishedProduct> findHTList(Map<String, Object> map);
}
