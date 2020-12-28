package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */

public interface WmsOutPurchaseReturnDetService extends IService<WmsOutPurchaseReturnDet> {

    List<WmsOutPurchaseReturnDetDto> findList(Map<String, Object> dynamicConditionByEntity);
}
