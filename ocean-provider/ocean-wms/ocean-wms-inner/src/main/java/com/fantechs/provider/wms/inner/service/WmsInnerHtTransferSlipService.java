package com.fantechs.provider.wms.inner.service;


import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/03/09.
 */

public interface WmsInnerHtTransferSlipService extends IService<WmsInnerHtTransferSlip> {
    List<WmsInnerHtTransferSlip> findHtList(Map<String, Object> map);
}
