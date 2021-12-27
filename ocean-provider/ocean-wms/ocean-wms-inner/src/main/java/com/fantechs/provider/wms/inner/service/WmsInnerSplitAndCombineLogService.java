package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerSplitAndCombineLog;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/27.
 */

public interface WmsInnerSplitAndCombineLogService extends IService<WmsInnerSplitAndCombineLog> {
    List<WmsInnerSplitAndCombineLog> findList(Map<String, Object> map);
}
