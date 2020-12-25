package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsInspectionItemDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */

public interface QmsInspectionItemDetService extends IService<QmsInspectionItemDet> {

    List<QmsInspectionItemDet> findList(Map<String, Object> map);
}
