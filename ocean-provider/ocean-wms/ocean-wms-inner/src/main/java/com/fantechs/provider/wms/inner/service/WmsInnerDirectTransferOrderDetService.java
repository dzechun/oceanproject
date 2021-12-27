package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/21.
 */

public interface WmsInnerDirectTransferOrderDetService extends IService<WmsInnerDirectTransferOrderDet> {
    List<WmsInnerDirectTransferOrderDetDto> findList(Map<String, Object> map);
}
