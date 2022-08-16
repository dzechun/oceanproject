package com.fantechs.provider.om.service;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */

public interface OmPurchaseOrderDetService extends IService<OmPurchaseOrderDet> {
    List<OmPurchaseOrderDetDto> findList(Map<String, Object> map);

    int batchAdd(List<OmPurchaseOrderDet> omPurchaseOrderDets);
}
