package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPurchaseReturn;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */

public interface WmsOutPurchaseReturnService extends IService<WmsOutPurchaseReturn> {


    List<WmsOutPurchaseReturnDto> findList(Map<String, Object> dynamicConditionByEntity);
}
