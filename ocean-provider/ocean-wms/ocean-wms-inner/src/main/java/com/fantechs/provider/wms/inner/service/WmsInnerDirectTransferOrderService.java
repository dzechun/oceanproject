package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/21.
 */

public interface WmsInnerDirectTransferOrderService extends IService<WmsInnerDirectTransferOrder> {
    List<WmsInnerDirectTransferOrderDto> findList(Map<String, Object> map);

    WmsInnerDirectTransferOrderDto detail(Long id);

    int save(List<PDAWmsInnerDirectTransferOrderDto> pdaWmsInnerDirectTransferOrderDtos);

    int check(List<PDAWmsInnerDirectTransferOrderDetDto> pdaWmsInnerDirectTransferOrderDetDtos);

}
