package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtBadItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/27.
 */

public interface QmsHtBadItemService extends IService<QmsHtBadItem> {
    List<QmsHtBadItem> findHtList(Map<String, Object> map);
}
