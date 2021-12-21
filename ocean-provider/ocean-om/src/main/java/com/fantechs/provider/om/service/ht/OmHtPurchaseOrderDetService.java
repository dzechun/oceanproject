package com.fantechs.provider.om.service.ht;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/20.
 */

public interface OmHtPurchaseOrderDetService extends IService<OmHtPurchaseOrderDet> {
    List<OmPurchaseOrderDetDto> findList(Map<String, Object> map);
}
