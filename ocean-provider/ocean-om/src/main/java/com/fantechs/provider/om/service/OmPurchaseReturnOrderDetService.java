package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrderDet;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/20.
 */

public interface OmPurchaseReturnOrderDetService extends IService<OmPurchaseReturnOrderDet> {
    List<OmPurchaseReturnOrderDetDto> findList(Map<String, Object> map);
    List<OmHtPurchaseReturnOrderDet> findHtList(Map<String, Object> map);
}
