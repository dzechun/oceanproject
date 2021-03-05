package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/25.
 */
public interface QmsHtInspectionItemService extends IService<QmsHtInspectionItem> {

    List<QmsHtInspectionItem> findHtList(Map<String, Object> map);
}
