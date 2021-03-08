package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/05.
 */

public interface WmsInnerTransferSlipService extends IService<WmsInnerTransferSlip> {
    List<WmsInnerTransferSlipDto> findList(Map<String, Object> map);
}
