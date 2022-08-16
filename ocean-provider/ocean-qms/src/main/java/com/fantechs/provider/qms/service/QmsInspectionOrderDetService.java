package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */

public interface QmsInspectionOrderDetService extends IService<QmsInspectionOrderDet> {
    List<QmsInspectionOrderDet> findList(Map<String, Object> map);
}
