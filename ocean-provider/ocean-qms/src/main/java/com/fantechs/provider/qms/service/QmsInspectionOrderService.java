package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */

public interface QmsInspectionOrderService extends IService<QmsInspectionOrder> {
    List<QmsInspectionOrder> findList(Map<String, Object> map);
    int writeBack(Long inspectionOrderId);
    int audit(QmsInspectionOrder qmsInspectionOrder);
    int autoAdd();
    int thirdInspection(QmsInspectionOrder qmsInspectionOrder);
    int batchQualified(Long inspectionOrderId);
    int batchSubmit(Long inspectionOrderId);
    QmsInspectionOrder selectByKey(Long key);
    int recheck(Long inspectionOrderId);
}
